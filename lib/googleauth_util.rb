require 'etc'
require 'googleauth'
require 'googleauth/stores/file_token_store'
require 'json'

module Google
  module Auth
    class Util
      CLIENT_SECRET = ENV['GOOGLE_AUTH_CLIENT_SECRET']
      TOKEN_STORE   = ENV['GOOGLE_AUTH_TOKEN_STORE']
      SCOPES        = ENV['GOOGLE_AUTH_SCOPES']
      OOB_URI       = 'urn:ietf:wg:oauth:2.0:oob'

      def initialize
        client_id = ClientId.from_file(CLIENT_SECRET)
        token_store = Stores::FileTokenStore.new(file: TOKEN_STORE)
        scopes = open(SCOPES) { |io| JSON.load(io) }
        @authorizer = UserAuthorizer.new(client_id, scopes, token_store)
      end

      def get_code
        url = @authorizer.get_authorization_url(base_url: OOB_URI)
        puts "Open #{url} in your browser and enter the resulting code:"
        gets
      end

      def credentials(user_id = Etc.getlogin || ENV['LOGNAME'])
        @authorizer.get_credentials(user_id) || \
        @authorizer.get_and_store_credentials_from_code(user_id: user_id,
                                                        code: get_code,
                                                        base_url: OOB_URI)
      end
    end
  end
end
