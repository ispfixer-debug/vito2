const functions = require('firebase-functions');
const admin = require('firebase-admin');
const crypto = require('crypto');

admin.initializeApp();

// Generate custom token from QR token
exports.generateCustomToken = functions.https.onCall(async (data, context) => {
  const { token, deviceId } = data;
  if (!token || !deviceId) throw new functions.https.HttpsError('invalid-argument', 'token and deviceId required');

  // Verify QR token exists
  const tokenDoc = await admin.firestore().collection('qr_tokens').doc(token).get();
  if (!tokenDoc.exists) throw new functions.https.HttpsError('not-found', 'Invalid QR token');
  
  const tokenData = tokenDoc.data();
  if (tokenData.used) throw new functions.https.HttpsError('already', 'Token already used');
  if (tokenData.expiresAt.toDate() < new Date()) throw new functions.https.HttpsError('expired', 'Token expired');

  // Mark as used
  await tokenDoc.ref.update({ used: true, deviceId, usedAt: admin.firestore.FieldValue.serverTimestamp() });

  // Create user document
  const uid = crypto.randomUUID();
  await admin.firestore().collection('users_v2').doc(uid).set({
    uid,
    alias: 'User' + Math.floor(Math.random() * 1000),
    role: tokenData.role || 'client',
    deviceId,
    walletBalance: 0,
    isVerified: false,
    accountStatus: 'active',
    createdAt: admin.firestore.FieldValue.serverTimestamp()
  });

  // Create custom token
  const customToken = await admin.auth().createCustomToken(uid);
  return { customToken, uid };
});

// Create payment intent (Stripe)
exports.createPaymentIntent = functions.https.onCall(async (data, context) => {
  const { amount, customerId } = data;
  if (!amount || amount < 50) throw new functions.https.HttpsError('invalid-argument', 'Minimum amount 50 cents');

  // In production, integrate with Stripe
  return { clientSecret: 'pi_mock_' + Date.now() };
});

// Request payout (driver)
exports.requestPayout = functions.https.onCall(async (data, context) => {
  const { amount } = data;
  const uid = context.auth?.uid;
  if (!uid) throw new functions.https.HttpsError('unauthenticated', 'Must be logged in');

  // Verify driver
  const userDoc = await admin.firestore().collection('users_v2').doc(uid).get();
  const user = userDoc.data();
  if (user?.role !== 'driver') throw new functions.https.HttpsError('permission-denied', 'Only drivers can request payout');

  // Create payout request
  const payoutRef = await admin.firestore().collection('payouts').add({
    userId: uid,
    amount,
    status: 'pending',
    createdAt: admin.firestore.FieldValue.serverTimestamp()
  });

  return { payoutId: payoutRef.id };
});

// Revoke token (admin)
exports.revokeToken = functions.https.onCall(async (data, context) => {
  const { token } = data;
  await admin.firestore().collection('qr_tokens').doc(token).update({ used: true });
  return { success: true };
});

// Cleanup old chats
exports.cleanupChats = functions.pubsub.schedule('every 24 hours').onRun(async () => {
  const cutoff = new Date(Date.now() - 24 * 60 * 60 * 1000);
  const oldChats = await admin.firestore().collection('chats').where('updatedAt', '<', cutoff).get();
  const batch = admin.firestore().batch();
  oldChats.docs.forEach(doc => batch.delete(doc.ref));
  await batch.commit();
  return null;
});

module.exports = exports;
