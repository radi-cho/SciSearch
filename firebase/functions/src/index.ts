import * as firestoreTriggers from "./algolia";

exports.onArticlePublished = firestoreTriggers.onArticlePublished;
exports.onArticleUpdated = firestoreTriggers.onArticleUpdated;
exports.onArticleDeleted = firestoreTriggers.onArticleDeleted;
