import * as functions from "firebase-functions";

const admin = require("firebase-admin");
admin.initializeApp();

const db = admin.firestore();
exports.getData = functions.https.onRequest((req, res) => {
  var articles = db.collection("articles");
  var query = articles
    .where("test", "==", true)
    .limit(1)
    .get()
    .then(snapshot => {
      snapshot.forEach(doc => {
        res.status(200).send(JSON.stringify(doc.data()));
        console.log(doc.id, "=>", doc.data());
      });
    })
    .catch(err => {
      console.log("Error getting documents", err);
    });
});
