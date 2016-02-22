(defproject yuhta/my-googleauth "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.8.0"]
                 [com.google.api-client/google-api-client-java6 "1.21.0"]]
  :profiles {:dev {:dependencies [[com.google.apis/google-api-services-oauth2 "v2-rev106-1.21.0"]]}}
  :global-vars {*warn-on-reflection* true})
