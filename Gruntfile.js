module.exports = function(grunt) {

    // Load grunt needed plugins
    grunt.loadNpmTasks('grunt-ember-templates');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-include-source');
    grunt.loadNpmTasks('grunt-wiredep');
    grunt.loadNpmTasks('grunt-contrib-copy');
    grunt.loadNpmTasks('grunt-sass');
    grunt.loadNpmTasks('grunt-contrib-clean');

    // Measure execution time for the tasks
    require('time-grunt')(grunt);

    // Initialize and configure grunt with a config object
    grunt.initConfig({
        
      // Ember templates compilation (details on: https://github.com/dgeb/grunt-ember-templates/pull/77)
      templateSrcFolder: 'src/main/templates',
      templateSrcFiles: '<%= templateSrcFolder %>/**/*.hbs',
      templateDstFile: 'src/main/javascript/web/js/templates.js',

      emberTemplates: {
          compile: {
              options: {
                  templateBasePath: '<%= templateSrcFolder %>',
                  templateCompilerPath: "bower_components/ember/ember-template-compiler.js", //Should use compiler with ember dist
                  handlebarsPath: "node_modules/handlebars/dist/handlebars.js" // Needed for template processor to pick compiler
              },
              files: {
                  '<%= templateDstFile %>': '<%= templateSrcFiles %>'
              }
          }
      },

      // Sass css compilation
      sassSrcFile: 'src/main/sass/main.scss',
      sassDstFile: 'src/main/html/web/main.css',
      sass: {
        options: {
          sourceMap: true
        },
        dist: {
          files: {
            '<%= sassDstFile %>': '<%= sassSrcFile %>'
          }
        }
      },

      // Auto-list own JS files in index.html
      indexHtmlTemplateLocation: 'src/main/templates/index.template.html',
      ownJsFolder: 'src/main/javascript/web',
      includeSource: {
          options: {
              basePath: '<%= ownJsFolder %>',
              baseUrl: ''
          },
          include_own_js: {
              files: {
                  'src/main/html/web/index.html': '<%= indexHtmlTemplateLocation %>'
              }
          }
      },

      // Auto-list bower JS files in index.html
      wiredep: {

          include_bower_js: {

              src: [
                  '<%= indexHtmlTemplateLocation %>'
              ],
              ignorePath: '../../../'
          }
      },

      bower_publish_folder: 'src/main/javascript/web/bower_components',
      // Delete published bower deps (needed to remove unused)
      clean:{
        bower_deps: ["<%= bower_publish_folder %>/*"]
      },

      // Make bower dependencies available in runtime
      copy: {
          bower_deps: {
              files: [
                  {
                      expand: true,
                      nonull: true,
                      cwd: 'bower_components/',
                      src: [
                          'jquery/dist/jquery.js',
                          'ember/ember.debug.js',
                          'ember-data/ember-data.js',
                          'marked/lib/marked.js',
                          'normalize.css/normalize.css',
                          'bootstrap/dist/css/bootstrap.css',
                          'bootstrap/dist/css/bootstrap-theme.css',
                          'bootstrap/dist/fonts/glyphicons-halflings-regular.woff',
                          'bootstrap/dist/js/bootstrap.js'
                      ],
                      dest: '<%= bower_publish_folder %>/'
                  }
              ]
          }
      },

      ownJsFiles: '<%= ownJsFolder %>/js/**/*.js',
      // Recompile templates if they change
      watch: {
        hbsRefresh: {
            files: '<%= templateSrcFiles %>',
            tasks: ['compile_hbs']
        },
        sassRefresh: {
          files: '<%= sassSrcFile %>',
          tasks: ['compile_sass']
        },
        own_js_refresh: {
          files: '<%= ownJsFiles %>',
          tasks: ['compile_index']
        },
        index_template_refresh: {
          files: '<%= indexHtmlTemplateLocation %>',
          tasks: ['compile_index']
        },
        bower_refresh: {
          files: 'bower.json',
          tasks: ['update_bower_deps']
        }
      }

    });
    
  // Basic task aliases (reference a single plugin task)
  grunt.registerTask('compile_hbs', ['emberTemplates']);
  grunt.registerTask('compile_sass', ['sass']);
  grunt.registerTask('compile_index', ['includeSource:include_own_js']);
  grunt.registerTask('update_bower_refs_in_template', ['wiredep:include_bower_js']);
  grunt.registerTask('clean_bower_deps', ['clean:bower_deps']);
  grunt.registerTask('publish_bower_deps', ['copy:bower_deps']);
  grunt.registerTask('rebuild_on_changes', ['watch']);


  // Aggregated task aliases (abstractions over a group of tasks)
  grunt.registerTask('update_bower_deps', ['clean_bower_deps','publish_bower_deps','update_bower_refs_in_template','compile_index']);
  grunt.registerTask('update_all_js_deps', ['update_bower_deps']); // Includes 'compile_index' that also updates own js deps

  // Main useful tasks (agregated tasks that have an important meaning or usage in teh project)
  grunt.registerTask('build', [
    'compile_sass',
    'compile_hbs',
    'update_all_js_deps'
  ]);

  // Default
  grunt.registerTask('default', ['build']);

};