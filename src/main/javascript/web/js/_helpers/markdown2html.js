Ember.Handlebars.helper('markdown2html', function(markdown) {
  if(!markdown){
    // Catch undefined values
    markdown = "";
  }
  return new Ember.Handlebars.SafeString(marked(markdown));
});