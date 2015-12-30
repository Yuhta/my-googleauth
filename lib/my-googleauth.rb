require 'googleauth'
require 'googleauth/stores/file_token_store'
require 'launchy'

module MyGoogleAuth
  include Google::Auth

  SCOPES = ['https://www.googleapis.com/auth/gmail.readonly',
            'https://www.googleapis.com/auth/calendar.readonly']
  OOB_URI = 'urn:ietf:wg:oauth:2.0:oob'

  def self.headers
    client_id = ClientId::from_file(ENV['GOOGLE_AUTH_CLIENT_SECRET'])
    token_store = Stores::FileTokenStore.new(file: ENV['GOOGLE_AUTH_TOKEN_STORE'])
    authorizer = UserAuthorizer.new(client_id, SCOPES, token_store)
    user_id = ENV['USER']
    credentials = authorizer.get_credentials(user_id)
    if credentials.nil?
      url = authorizer.get_authorization_url(base_url: OOB_URI)
      puts "Open #{url} in your browser and enter the resulting code:"
      Launchy.open url
      code = gets
      credentials = authorizer.get_and_store_credentials_from_code(user_id: user_id,
                                                                   code: code,
                                                                   base_url: OOB_URI)
    end
    hs = {}
    credentials.apply({}).each { |k, v| hs[k.to_s] = v }
    hs
  end
end
