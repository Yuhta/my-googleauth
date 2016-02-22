(ns my-googleauth.core
  (:require [clojure.java.io :refer [file reader]])
  (:import com.google.api.client.auth.oauth2.Credential
           (com.google.api.client.extensions.java6.auth.oauth2 AuthorizationCodeInstalledApp
                                                               AbstractPromptReceiver)
           (com.google.api.client.googleapis auth.oauth2.GoogleAuthorizationCodeFlow$Builder
                                             auth.oauth2.GoogleClientSecrets
                                             auth.oauth2.GoogleOAuthConstants
                                             javanet.GoogleNetHttpTransport
                                             util.Utils)
           (com.google.api.client.util.store DataStoreFactory
                                             FileDataStoreFactory)))

(def ^:dynamic *google-auth-home*
  (file (System/getProperty "user.home") ".google-auth"))

(def ^:dynamic ^DataStoreFactory *data-store-factory*
  (FileDataStoreFactory. *google-auth-home*))

(def ^:dynamic ^GoogleClientSecrets *client-secret*
  (GoogleClientSecrets/load (Utils/getDefaultJsonFactory)
                            (reader (file *google-auth-home*
                                          "client-secret.json"))))

(def ^:dynamic *scopes*
  #{})

(def ^:dynamic *http-transport*
  (GoogleNetHttpTransport/newTrustedTransport))

(def ^:private prompt-receiver
  (proxy [AbstractPromptReceiver] []
    (getRedirectUri [] GoogleOAuthConstants/OOB_REDIRECT_URI)))

(defn ^Credential authorize []
  (let [flow (-> (GoogleAuthorizationCodeFlow$Builder. *http-transport*
                                                       (Utils/getDefaultJsonFactory)
                                                       *client-secret*
                                                       *scopes*)
                 (.setDataStoreFactory *data-store-factory*)
                 .build)]
    (-> (AuthorizationCodeInstalledApp. flow prompt-receiver)
        (.authorize (System/getProperty "user.name")))))
