(ns yuhta.googleauth-test
  (:require [clojure.test :refer :all]
            [clojure.java.io :refer [reader resource]]
            [yuhta.googleauth :refer :all])
  (:import (com.google.api.client.googleapis auth.oauth2.GoogleClientSecrets
                                             util.Utils)
           com.google.api.client.util.store.MemoryDataStoreFactory
           com.google.api.services.oauth2.Oauth2$Builder))

(use-fixtures :once
  #(with-redefs [*client-secret* (GoogleClientSecrets/load (Utils/getDefaultJsonFactory)
                                                           (reader (resource "client-secret.json")))
                 *data-store-factory* (MemoryDataStoreFactory/getDefaultInstance)
                 *scopes* #{"https://www.googleapis.com/auth/userinfo.profile",
                            "https://www.googleapis.com/auth/userinfo.email"}]
     (%)))

(deftest test-oauth2
  (let [credential (authorize)
        oauth2 (-> (Oauth2$Builder. *http-transport*
                                    (Utils/getDefaultJsonFactory)
                                    credential)
                   (.setApplicationName "googleauth-test")
                   .build)
        token-info (-> oauth2
                       .tokeninfo
                       (.setAccessToken (.getAccessToken credential))
                       .execute)]
    (println (.toPrettyString token-info))
    (println (-> oauth2 .userinfo .get .execute .toPrettyString))
    (is (= (.getAudience token-info)
           (-> *client-secret* .getDetails .getClientId)))))
