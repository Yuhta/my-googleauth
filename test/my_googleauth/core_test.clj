(ns my-googleauth.core-test
  (:require [clojure.test :refer :all]
            [my-googleauth.core :refer :all])
  (:import com.google.api.client.googleapis.util.Utils
           com.google.api.client.util.store.MemoryDataStoreFactory
           com.google.api.services.oauth2.Oauth2$Builder))

(use-fixtures :once
  #(with-redefs [*data-store-factory* (MemoryDataStoreFactory/getDefaultInstance)
                 *scopes* #{"https://www.googleapis.com/auth/userinfo.profile",
                            "https://www.googleapis.com/auth/userinfo.email"}]
     (%)))

(deftest test-oauth2
  (let [credential (authorize)
        oauth2 (-> (Oauth2$Builder. *http-transport*
                                    (Utils/getDefaultJsonFactory)
                                    credential)
                   (.setApplicationName "my-googleauth.core-test")
                   .build)
        token-info (-> oauth2
                       .tokeninfo
                       (.setAccessToken (.getAccessToken credential))
                       .execute)]
    (println (.toPrettyString token-info))
    (println (-> oauth2 .userinfo .get .execute .toPrettyString))
    (is (= (.getAudience token-info)
           (-> *client-secret* .getDetails .getClientId)))))
