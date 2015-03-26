Ember.Handlebars.helper('markdown2html', function(markdown) {
  return new Ember.Handlebars.SafeString(marked(markdown));
});