module.exports = function(grunt) {

    // Load grunt needed plugins
    grunt.loadNpmTasks('grunt-ember-templates');
    grunt.loadNpmTasks('grunt-contrib-watch');
    grunt.loadNpmTasks('grunt-include-source');
    grunt.loadNpmTasks('grunt-wiredep');
    grunt.loadNpmTasks('grunt-contrib-copy');

    // Initialize and configure grunt with a config object
    grunt.initConfig({
        
        // Ember templates compilation (details on: https://github.com/dgeb/grunt-ember-templates/pull/77)
        templateSrcFolder: 'src/main/templates',
        templateDstFile: 'src/main/javascript/web/js/templates.js',
        
        templateSrcFiles: '<%= templateSrcFolder %>/**/*.hbs',
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
        
        // Make bower dependencies available in runtime
        copy: {
            publish_bower_js: {
                files: [
                    {
                        expand: true,
                        nonull: true,
                        cwd: 'bower_components/',
                        src: [
                            'jquery/dist/jquery.js',
                            'ember/ember.debug.js',
                            'ember-data/ember-data.js'
                        ],
                        dest: 'src/main/javascript/web/bower_components/'
                    }
                ]
            }
        },

        // Recompile templates if they change
        watch: {
            emberTemplates: {
                files: '<%= templateSrcFiles %>',
                tasks: ['emberTemplates']
            },
            own_js: {
              files: '<%= ownJsFolder %>/js/**/*.js',
              tasks: ['update_js_deps']
            }
        }

    });
    
    
    
    // Define runnable tasks
    grunt.registerTask('default', ['emberTemplates']);
    grunt.registerTask('compile_and_watch', ['emberTemplates', 'watch']);
    grunt.registerTask('update_js_deps', ['wiredep:include_bower_js','includeSource:include_own_js','copy:publish_bower_js']);
    grunt.registerTask('setup_project', ['emberTemplates', 'update_js_deps']);

};