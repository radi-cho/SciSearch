import * as functions from "firebase-functions";
import * as algoliasearch from "algoliasearch";

const admin = require("firebase-admin");
admin.initializeApp();
const db = admin.firestore();

const ALGOLIA_ID = functions.config().algolia.appid;
const ALGOLIA_ADMIN_KEY = functions.config().algolia.adminkey;
const ALGOLIA_SEARCH_KEY = functions.config().algolia.apikey;

const ALGOLIA_INDEX_NAME = "articles";
const algolia = algoliasearch(ALGOLIA_ID, ALGOLIA_ADMIN_KEY);
const index = algolia.initIndex(ALGOLIA_INDEX_NAME);

const saveAlgoliaObject = (data, id) => {
  const document = data;
  document.objectID = id;
  return index.saveObject(document);
};

export const onArticlePublished = functions.firestore
  .document("articles/{articleId}")
  .onCreate((snap, context) => {
    return saveAlgoliaObject(snap.data(), context.params.articleId);
  });

export const onArticleUpdated = functions.firestore
  .document("articles/{articleId}")
  .onUpdate((change, context) => {
    return saveAlgoliaObject(change.after.data(), context.params.articleId);
  });

export const onArticleDeleted = functions.firestore
  .document("articles/{articleId}")
  .onDelete((snap, context) => {
    const articleId = snap.id;
    return index.deleteObject(articleId);
  });
