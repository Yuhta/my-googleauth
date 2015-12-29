Gem::Specification.new do |s|
  s.name = 'my-googleauth'
  s.version = '0.1.0'
  s.summary = 'Google Auth utilities'
  s.authors = ['Jimmy Lu']
  s.files = Dir['lib/**/*']
  s.add_runtime_dependency 'googleauth'
  s.add_runtime_dependency 'launchy', '~> 2.4'
end
