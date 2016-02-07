"use strict";
/* jshint ignore:start */

/* jshint ignore:end */

define('my-new-app/adapters/application', ['exports', 'ember', 'ember-data', 'my-new-app/routes/login'], function (exports, Ember, DS, loginRoute) {

  'use strict';

  exports['default'] = DS['default'].RESTAdapter.extend({
    namespace: 'api/v1',
    shouldReloadAll: function shouldReloadAll() {
      // We want the store to re-fetch the backend every time findAll is called (instead of using cached versions)
      return true;
    },
    shouldReloadRecord: function shouldReloadRecord() {
      // We want the store to re-fetch the backend every time find is called (instead of using cached versions)
      return true;
    }
  });

});
define('my-new-app/app', ['exports', 'ember', 'ember/resolver', 'ember/load-initializers', 'my-new-app/config/environment'], function (exports, Ember, Resolver, loadInitializers, config) {

  'use strict';

  var App;

  Ember['default'].MODEL_FACTORY_INJECTIONS = true;

  App = Ember['default'].Application.extend({
    modulePrefix: config['default'].modulePrefix,
    podModulePrefix: config['default'].podModulePrefix,
    Resolver: Resolver['default'],
    LOG_TRANSITIONS: true
  });

  //LOG_TRANSITIONS_INTERNAL: true // Log route hooks and transition
  loadInitializers['default'](App, config['default'].modulePrefix);

  exports['default'] = App;

});
define('my-new-app/components/app-version', ['exports', 'ember-cli-app-version/components/app-version', 'my-new-app/config/environment'], function (exports, AppVersionComponent, config) {

  'use strict';

  var _config$APP = config['default'].APP;
  var name = _config$APP.name;
  var version = _config$APP.version;

  exports['default'] = AppVersionComponent['default'].extend({
    version: version,
    name: name
  });

});
define('my-new-app/components/labeled-label', ['exports', 'ember'], function (exports, Ember) {

  'use strict';

  exports['default'] = Ember['default'].Component.extend({
    tagName: 'p',
    classNames: ['property-input']
  });

});
define('my-new-app/components/markdown-view', ['exports', 'ember'], function (exports, Ember) {

	'use strict';

	exports['default'] = Ember['default'].Component.extend({});

});
define('my-new-app/components/navigation-bar', ['exports', 'ember'], function (exports, Ember) {

  'use strict';

  exports['default'] = Ember['default'].Component.extend({
    tagName: 'nav',
    classNames: ['navbar', 'navbar-default', 'navbar-fixed-top'],
    action: 'logout',
    actions: {
      logout: function logout() {
        //Send the default action that is 'logout'
        this.sendAction('action');
      }
    }
  });

});
define('my-new-app/controllers/array', ['exports', 'ember'], function (exports, Ember) {

	'use strict';

	exports['default'] = Ember['default'].Controller;

});
define('my-new-app/controllers/login', ['exports', 'ember'], function (exports, Ember) {

  'use strict';

  exports['default'] = Ember['default'].Controller.extend({
    actions: {
      logIn: function logIn() {
        var self = this;

        var credentials = this.get('model');
        return Ember['default'].$.post("/j_security_check", {
          j_username: credentials.login,
          j_password: credentials.password }).then(function () {
          self.set('authenticated', true);

          // Continue with previous transition if any
          var previousTransition = self.get('previousTransition');
          if (previousTransition) {
            self.set('previousTransition', null);
            previousTransition.retry();
          } else {
            // Default back to homepage
            self.transitionToRoute('index');
          }
        }, function (response) {
          var statusCode = response.status;
          var errorMessage;
          if (statusCode === 401) {
            errorMessage = "Invalid credentials";
          } else {
            errorMessage = "Unknown error: " + statusCode + " - " + response.statusText;
          }
          self.set("errorMessage", errorMessage);
        });
      }
    }
  });

});
define('my-new-app/controllers/object', ['exports', 'ember'], function (exports, Ember) {

	'use strict';

	exports['default'] = Ember['default'].Controller;

});
define('my-new-app/controllers/procedures/edit', ['exports', 'ember'], function (exports, Ember) {

  'use strict';

  exports['default'] = Ember['default'].ObjectController.extend({
    actions: {
      saveProcedure: function saveProcedure() {
        var procedure = this.get('model');
        procedure.save();
        this.transitionToRoute('procedures.view', procedure);
      },
      deleteProcedure: function deleteProcedure() {
        var model = this.get('model');
        model.destroyRecord();
        this.transitionToRoute('procedures');
      },
      cancelEdition: function cancelEdition() {
        var procedure = this.get('model');
        this.transitionToRoute('procedures.view', procedure);
      }
    }
  });

});
define('my-new-app/controllers/procedures/view', ['exports', 'ember'], function (exports, Ember) {

  'use strict';

  exports['default'] = Ember['default'].Controller.extend({
    actions: {
      editProcedure: function editProcedure(procedure) {
        this.transitionToRoute('procedures.edit', procedure);
      },
      deleteProcedure: function deleteProcedure(procedure) {
        procedure.destroyRecord();
        this.transitionToRoute('procedures');
      }
    }
  });

});
define('my-new-app/controllers/procedures', ['exports', 'ember'], function (exports, Ember) {

  'use strict';

  exports['default'] = Ember['default'].Controller.extend({
    queryParams: ['nameOrDescription'],
    nameOrDescription: "",
    actions: {
      createProcedure: function createProcedure() {
        var newRecord = this.store.createRecord('procedure', {});
        newRecord.save();
        this.transitionToRoute('procedures.edit', newRecord);
      }
    },

    // We can do this while we have all the necessary data and the set is small enough
    filteredProcedures: Ember['default'].computed('model', 'nameOrDescription', function () {
      var nameOrDescription = this.get('nameOrDescription');
      var procedures = this.get('model');

      if (!nameOrDescription) {
        return procedures;
      }
      return procedures.filter(function (item) {
        var foundInName = item.get("name").indexOf(nameOrDescription) > -1;
        var foundInDescription = item.get("description").indexOf(nameOrDescription) > -1;
        return foundInName || foundInDescription;
      });
    })
  });

});
define('my-new-app/controllers/users/edit', ['exports', 'ember'], function (exports, Ember) {

  'use strict';

  exports['default'] = Ember['default'].Controller.extend({
    actions: {
      save: function save() {
        this.get('model').save();
      },
      remove: function remove() {
        var model = this.get('model');
        model.deleteRecord();
        model.save();
      }
    }
  });

});
define('my-new-app/controllers/users', ['exports', 'ember'], function (exports, Ember) {

  'use strict';

  exports['default'] = Ember['default'].Controller.extend({
    actions: {
      create: function create() {
        var newUser = this.store.createRecord('user', {});
        newUser.save();
      }
    }
  });

});
define('my-new-app/helpers/markdown-ashtml', ['exports', 'ember'], function (exports, Ember) {

  'use strict';

  exports.markdownAshtml = markdownAshtml;

  function markdownAshtml(params /*, hash*/) {
    var value = params[0];
    if (!value) {
      // Catch undefined values
      value = "";
    }
    return new Ember['default'].Handlebars.SafeString(marked(value));
  }

  exports['default'] = Ember['default'].Helper.helper(markdownAshtml);

});
define('my-new-app/initializers/app-version', ['exports', 'ember-cli-app-version/initializer-factory', 'my-new-app/config/environment'], function (exports, initializerFactory, config) {

  'use strict';

  var _config$APP = config['default'].APP;
  var name = _config$APP.name;
  var version = _config$APP.version;

  exports['default'] = {
    name: 'App Version',
    initialize: initializerFactory['default'](name, version)
  };

});
define('my-new-app/initializers/export-application-global', ['exports', 'ember', 'my-new-app/config/environment'], function (exports, Ember, config) {

  'use strict';

  exports.initialize = initialize;

  function initialize() {
    var application = arguments[1] || arguments[0];
    if (config['default'].exportApplicationGlobal !== false) {
      var value = config['default'].exportApplicationGlobal;
      var globalName;

      if (typeof value === 'string') {
        globalName = value;
      } else {
        globalName = Ember['default'].String.classify(config['default'].modulePrefix);
      }

      if (!window[globalName]) {
        window[globalName] = application;

        application.reopen({
          willDestroy: function willDestroy() {
            this._super.apply(this, arguments);
            delete window[globalName];
          }
        });
      }
    }
  }

  exports['default'] = {
    name: 'export-application-global',

    initialize: initialize
  };

});
define('my-new-app/mixins/authenticated-route', ['exports', 'ember'], function (exports, Ember) {

  'use strict';

  exports['default'] = Ember['default'].Mixin.create({
    beforeModel: function beforeModel(transition) {
      var loginController = this.controllerFor('login');
      if (!loginController.get('authenticated')) {
        loginController.set('previousTransition', transition);
        this.transitionTo('login');
      }
    }
  });

});
define('my-new-app/models/procedure', ['exports', 'ember-data'], function (exports, DS) {

  'use strict';

  exports['default'] = DS['default'].Model.extend({
    name: DS['default'].attr('string'),
    description: DS['default'].attr('string')
  });

});
define('my-new-app/models/user', ['exports', 'ember-data'], function (exports, DS) {

  'use strict';

  exports['default'] = DS['default'].Model.extend({
    name: DS['default'].attr('string'),
    login: DS['default'].attr('string'),
    password: DS['default'].attr('string'),
    creation: DS['default'].attr('string'),
    modification: DS['default'].attr('string')
  });

});
define('my-new-app/router', ['exports', 'ember', 'my-new-app/config/environment'], function (exports, Ember, config) {

  'use strict';

  var Router = Ember['default'].Router.extend({
    location: config['default'].locationType
  });

  Router.map(function () {

    this.route('login');

    this.route('users', function () {
      this.route('edit', { path: "edit/:user_id" });
    });

    this.route('procedures', function () {
      this.route('view', { path: "view/:procedure_id" });
      this.route('edit', { path: "edit/:procedure_id" });
    });

    // Catches all the malformed urls (not matching previous routes)
    this.route('wrong-paths', { path: '/*wrong-paths' });
  });

  exports['default'] = Router;

});
define('my-new-app/routes/application', ['exports', 'ember'], function (exports, Ember) {

  'use strict';

  exports['default'] = Ember['default'].Route.extend({
    actions: {
      /**
       * This error handler can recover from unauthenticated requests while transitioning to a route.
       * It tries to authenticate first, and then will retry the transition.
       * Helpful for non authenticated routes that make authenticated requests
       */
      error: function error(_error, transition) {
        if (_error.status === '401' || _error.errors && _error.errors[0].status === '401') {
          //It's an authentication problem? Try to authenticate first
          var loginController = this.controllerFor('login');
          loginController.set('previousTransition', transition);
          this.transitionTo('login');
        } else {
          // For other error, just log it
          console.log("Route error:");
          console.log(_error);
          console.log("Failed transition:");
          console.log(transition);
          return this._super(_error, transition);
        }
      },
      logout: function logout() {
        var self = this;
        Ember['default'].$.post("/j_logout", {}).then(function (response) {
          var loginController = self.controllerFor('login');
          loginController.set('authenticated', null);
          self.transitionTo('login');
        }, function (response) {
          console.log("Error logging out");
          console.log(response);
        });
      }
    }
  });

});
define('my-new-app/routes/index', ['exports', 'ember'], function (exports, Ember) {

  'use strict';

  exports['default'] = Ember['default'].Route.extend({
    beforeModel: function beforeModel() {
      this.transitionTo('procedures');
    }
  });

});
define('my-new-app/routes/login', ['exports', 'ember'], function (exports, Ember) {

  'use strict';

  exports['default'] = Ember['default'].Route.extend({
    model: function model() {
      return { login: '', password: '' };
    }
  });

});
define('my-new-app/routes/procedures/edit', ['exports', 'ember', 'my-new-app/mixins/authenticated-route'], function (exports, Ember, AuthenticatedRoute) {

	'use strict';

	exports['default'] = Ember['default'].Route.extend(AuthenticatedRoute['default'], {});

});
define('my-new-app/routes/procedures/view', ['exports', 'ember', 'my-new-app/mixins/authenticated-route'], function (exports, Ember, AuthenticatedRoute) {

	'use strict';

	exports['default'] = Ember['default'].Route.extend(AuthenticatedRoute['default'], {});

});
define('my-new-app/routes/procedures', ['exports', 'ember', 'my-new-app/mixins/authenticated-route'], function (exports, Ember, AuthenticatedRoute) {

  'use strict';

  exports['default'] = Ember['default'].Route.extend(AuthenticatedRoute['default'], {
    model: function model() {
      return this.store.findAll('procedure');
    }
  });

});
define('my-new-app/routes/users', ['exports', 'ember', 'my-new-app/mixins/authenticated-route'], function (exports, Ember, AuthenticatedRoute) {

  'use strict';

  exports['default'] = Ember['default'].Route.extend(AuthenticatedRoute['default'], {
    model: function model() {
      return this.store.findAll('user');
    }
  });

});
define('my-new-app/serializers/application', ['exports', 'ember-data'], function (exports, DS) {

  'use strict';

  exports['default'] = DS['default'].RESTSerializer.extend({
    extractArray: function extractArray(store, type, payload) {
      var payloadTemp = {};
      payloadTemp[type.typeKey] = payload;
      return this._super(store, type, payloadTemp);
    },
    extractSingle: function extractSingle(store, type, payload, id) {
      var payloadTemp = {};
      payloadTemp[type.typeKey] = [payload];
      return this._super(store, type, payloadTemp, id);
    },
    serializeIntoHash: function serializeIntoHash(hash, type, snapshot, options) {
      var serialized = this.serialize(snapshot, options);
      serialized["id"] = snapshot.id;
      Ember.$.extend(hash, serialized);
    }
  });

});
define('my-new-app/templates/application', ['exports'], function (exports) {

  'use strict';

  exports['default'] = Ember.HTMLBars.template((function() {
    return {
      meta: {
        "revision": "Ember@1.13.7",
        "loc": {
          "source": null,
          "start": {
            "line": 1,
            "column": 0
          },
          "end": {
            "line": 2,
            "column": 0
          }
        },
        "moduleName": "my-new-app/templates/application.hbs"
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createComment("");
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var morphs = new Array(1);
        morphs[0] = dom.createMorphAt(fragment,0,0,contextualElement);
        dom.insertBoundary(fragment, 0);
        return morphs;
      },
      statements: [
        ["content","outlet",["loc",[null,[1,0],[1,10]]]]
      ],
      locals: [],
      templates: []
    };
  }()));

});
define('my-new-app/templates/components/labeled-label', ['exports'], function (exports) {

  'use strict';

  exports['default'] = Ember.HTMLBars.template((function() {
    return {
      meta: {
        "revision": "Ember@1.13.7",
        "loc": {
          "source": null,
          "start": {
            "line": 1,
            "column": 0
          },
          "end": {
            "line": 3,
            "column": 0
          }
        },
        "moduleName": "my-new-app/templates/components/labeled-label.hbs"
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createElement("span");
        var el2 = dom.createComment("");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("span");
        var el2 = dom.createComment("");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var morphs = new Array(2);
        morphs[0] = dom.createMorphAt(dom.childAt(fragment, [0]),0,0);
        morphs[1] = dom.createMorphAt(dom.childAt(fragment, [2]),0,0);
        return morphs;
      },
      statements: [
        ["content","label",["loc",[null,[1,6],[1,15]]]],
        ["content","value",["loc",[null,[2,6],[2,15]]]]
      ],
      locals: [],
      templates: []
    };
  }()));

});
define('my-new-app/templates/components/markdown-view', ['exports'], function (exports) {

  'use strict';

  exports['default'] = Ember.HTMLBars.template((function() {
    return {
      meta: {
        "revision": "Ember@1.13.7",
        "loc": {
          "source": null,
          "start": {
            "line": 1,
            "column": 0
          },
          "end": {
            "line": 1,
            "column": 25
          }
        },
        "moduleName": "my-new-app/templates/components/markdown-view.hbs"
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createComment("");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var morphs = new Array(1);
        morphs[0] = dom.createMorphAt(fragment,0,0,contextualElement);
        dom.insertBoundary(fragment, 0);
        dom.insertBoundary(fragment, null);
        return morphs;
      },
      statements: [
        ["inline","markdown-ashtml",[["get","value",["loc",[null,[1,18],[1,23]]]]],[],["loc",[null,[1,0],[1,25]]]]
      ],
      locals: [],
      templates: []
    };
  }()));

});
define('my-new-app/templates/components/navigation-bar', ['exports'], function (exports) {

  'use strict';

  exports['default'] = Ember.HTMLBars.template((function() {
    var child0 = (function() {
      return {
        meta: {
          "revision": "Ember@1.13.7",
          "loc": {
            "source": null,
            "start": {
              "line": 17,
              "column": 8
            },
            "end": {
              "line": 17,
              "column": 41
            }
          },
          "moduleName": "my-new-app/templates/components/navigation-bar.hbs"
        },
        arity: 0,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode("Procesos");
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes() { return []; },
        statements: [

        ],
        locals: [],
        templates: []
      };
    }());
    var child1 = (function() {
      return {
        meta: {
          "revision": "Ember@1.13.7",
          "loc": {
            "source": null,
            "start": {
              "line": 22,
              "column": 12
            },
            "end": {
              "line": 22,
              "column": 43
            }
          },
          "moduleName": "my-new-app/templates/components/navigation-bar.hbs"
        },
        arity: 0,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode("Administrar");
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes() { return []; },
        statements: [

        ],
        locals: [],
        templates: []
      };
    }());
    return {
      meta: {
        "revision": "Ember@1.13.7",
        "loc": {
          "source": null,
          "start": {
            "line": 1,
            "column": 0
          },
          "end": {
            "line": 30,
            "column": 0
          }
        },
        "moduleName": "my-new-app/templates/components/navigation-bar.hbs"
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createComment(" Logo space ");
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("div");
        dom.setAttribute(el1,"class","navbar-header");
        var el2 = dom.createTextNode("\n  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createComment(" Responsive collapsible menu ");
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("button");
        dom.setAttribute(el2,"type","button");
        dom.setAttribute(el2,"class","navbar-toggle collapsed");
        dom.setAttribute(el2,"data-toggle","collapse");
        dom.setAttribute(el2,"data-target","#navbar");
        dom.setAttribute(el2,"aria-expanded","false");
        dom.setAttribute(el2,"aria-controls","navbar");
        var el3 = dom.createTextNode("\n    ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("span");
        dom.setAttribute(el3,"class","sr-only");
        var el4 = dom.createTextNode("Toggle navigation");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n    ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("span");
        dom.setAttribute(el3,"class","icon-bar");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n    ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("span");
        dom.setAttribute(el3,"class","icon-bar");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n    ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("span");
        dom.setAttribute(el3,"class","icon-bar");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n  ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("span");
        dom.setAttribute(el2,"class","navbar-brand");
        var el3 = dom.createTextNode("Procesos activos");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createComment(" Navigation options ");
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("div");
        dom.setAttribute(el1,"id","navbar");
        dom.setAttribute(el1,"class","navbar-collapse collapse");
        var el2 = dom.createTextNode("\n  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("ul");
        dom.setAttribute(el2,"class","nav navbar-nav");
        var el3 = dom.createTextNode("\n    ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("li");
        var el4 = dom.createElement("a");
        var el5 = dom.createTextNode("Inicio");
        dom.appendChild(el4, el5);
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n    ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("li");
        var el4 = dom.createComment("");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n    ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("li");
        var el4 = dom.createElement("a");
        var el5 = dom.createTextNode("Labores");
        dom.appendChild(el4, el5);
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n    ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("li");
        dom.setAttribute(el3,"class","dropdown");
        var el4 = dom.createTextNode("\n      ");
        dom.appendChild(el3, el4);
        var el4 = dom.createElement("a");
        dom.setAttribute(el4,"href","#");
        dom.setAttribute(el4,"class","dropdown-toggle");
        dom.setAttribute(el4,"data-toggle","dropdown");
        dom.setAttribute(el4,"role","button");
        dom.setAttribute(el4,"aria-expanded","false");
        var el5 = dom.createTextNode("Usuarios");
        dom.appendChild(el4, el5);
        var el5 = dom.createElement("span");
        dom.setAttribute(el5,"class","caret");
        dom.appendChild(el4, el5);
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("\n      ");
        dom.appendChild(el3, el4);
        var el4 = dom.createElement("ul");
        dom.setAttribute(el4,"class","dropdown-menu");
        dom.setAttribute(el4,"role","menu");
        var el5 = dom.createTextNode("\n        ");
        dom.appendChild(el4, el5);
        var el5 = dom.createElement("li");
        var el6 = dom.createComment("");
        dom.appendChild(el5, el6);
        dom.appendChild(el4, el5);
        var el5 = dom.createTextNode("\n        ");
        dom.appendChild(el4, el5);
        var el5 = dom.createElement("li");
        dom.setAttribute(el5,"class","divider");
        dom.appendChild(el4, el5);
        var el5 = dom.createTextNode("\n        ");
        dom.appendChild(el4, el5);
        var el5 = dom.createElement("li");
        dom.setAttribute(el5,"class","dropdown-header");
        var el6 = dom.createTextNode("Nav header");
        dom.appendChild(el5, el6);
        dom.appendChild(el4, el5);
        var el5 = dom.createTextNode("\n        ");
        dom.appendChild(el4, el5);
        var el5 = dom.createElement("li");
        var el6 = dom.createElement("a");
        dom.setAttribute(el6,"href","#");
        var el7 = dom.createTextNode("Logout");
        dom.appendChild(el6, el7);
        dom.appendChild(el5, el6);
        dom.appendChild(el4, el5);
        var el5 = dom.createTextNode("\n      ");
        dom.appendChild(el4, el5);
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("\n    ");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n  ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var element0 = dom.childAt(fragment, [6, 1]);
        var element1 = dom.childAt(element0, [7, 3]);
        var element2 = dom.childAt(element1, [7, 0]);
        var morphs = new Array(3);
        morphs[0] = dom.createMorphAt(dom.childAt(element0, [3]),0,0);
        morphs[1] = dom.createMorphAt(dom.childAt(element1, [1]),0,0);
        morphs[2] = dom.createElementMorph(element2);
        return morphs;
      },
      statements: [
        ["block","link-to",["procedures"],[],0,null,["loc",[null,[17,8],[17,53]]]],
        ["block","link-to",["users"],[],1,null,["loc",[null,[22,12],[22,55]]]],
        ["element","action",["logout"],[],["loc",[null,[25,25],[25,44]]]]
      ],
      locals: [],
      templates: [child0, child1]
    };
  }()));

});
define('my-new-app/templates/index', ['exports'], function (exports) {

  'use strict';

  exports['default'] = Ember.HTMLBars.template((function() {
    return {
      meta: {
        "revision": "Ember@1.13.7",
        "loc": {
          "source": null,
          "start": {
            "line": 1,
            "column": 0
          },
          "end": {
            "line": 2,
            "column": 0
          }
        },
        "moduleName": "my-new-app/templates/index.hbs"
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createComment("");
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var morphs = new Array(1);
        morphs[0] = dom.createMorphAt(fragment,0,0,contextualElement);
        dom.insertBoundary(fragment, 0);
        return morphs;
      },
      statements: [
        ["content","outlet",["loc",[null,[1,0],[1,10]]]]
      ],
      locals: [],
      templates: []
    };
  }()));

});
define('my-new-app/templates/login', ['exports'], function (exports) {

  'use strict';

  exports['default'] = Ember.HTMLBars.template((function() {
    var child0 = (function() {
      return {
        meta: {
          "revision": "Ember@1.13.7",
          "loc": {
            "source": null,
            "start": {
              "line": 3,
              "column": 6
            },
            "end": {
              "line": 7,
              "column": 6
            }
          },
          "moduleName": "my-new-app/templates/login.hbs"
        },
        arity: 0,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode("        ");
          dom.appendChild(el0, el1);
          var el1 = dom.createElement("div");
          dom.setAttribute(el1,"class","error-message");
          var el2 = dom.createTextNode("\n            ");
          dom.appendChild(el1, el2);
          var el2 = dom.createComment("");
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n        ");
          dom.appendChild(el1, el2);
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode("\n");
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
          var morphs = new Array(1);
          morphs[0] = dom.createMorphAt(dom.childAt(fragment, [1]),1,1);
          return morphs;
        },
        statements: [
          ["content","errorMessage",["loc",[null,[5,12],[5,28]]]]
        ],
        locals: [],
        templates: []
      };
    }());
    return {
      meta: {
        "revision": "Ember@1.13.7",
        "loc": {
          "source": null,
          "start": {
            "line": 1,
            "column": 0
          },
          "end": {
            "line": 13,
            "column": 7
          }
        },
        "moduleName": "my-new-app/templates/login.hbs"
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createElement("form");
        dom.setAttribute(el1,"class","form-signin");
        var el2 = dom.createTextNode("\n    ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("h2");
        dom.setAttribute(el2,"class","form-signin-heading");
        var el3 = dom.createTextNode("Login");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n");
        dom.appendChild(el1, el2);
        var el2 = dom.createComment("");
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("    ");
        dom.appendChild(el1, el2);
        var el2 = dom.createComment("");
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n    ");
        dom.appendChild(el1, el2);
        var el2 = dom.createComment("");
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n    ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("button");
        dom.setAttribute(el2,"class","btn btn-lg btn-primary btn-block");
        var el3 = dom.createTextNode("Entrar");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var element0 = dom.childAt(fragment, [0]);
        var element1 = dom.childAt(element0, [9]);
        var morphs = new Array(4);
        morphs[0] = dom.createMorphAt(element0,3,3);
        morphs[1] = dom.createMorphAt(element0,5,5);
        morphs[2] = dom.createMorphAt(element0,7,7);
        morphs[3] = dom.createElementMorph(element1);
        return morphs;
      },
      statements: [
        ["block","if",[["get","errorMessage",["loc",[null,[3,12],[3,24]]]]],[],0,null,["loc",[null,[3,6],[7,13]]]],
        ["inline","input",[],["class","form-control","placeholder","login name","value",["subexpr","@mut",[["get","model.login",["loc",[null,[9,14],[9,25]]]]],[],[]]],["loc",[null,[8,4],[9,27]]]],
        ["inline","input",[],["class","form-control","placeholder","password","type","password","value",["subexpr","@mut",[["get","model.password",["loc",[null,[11,14],[11,28]]]]],[],[]],"enter","logIn"],["loc",[null,[10,4],[11,44]]]],
        ["element","action",["logIn"],[],["loc",[null,[12,53],[12,71]]]]
      ],
      locals: [],
      templates: [child0]
    };
  }()));

});
define('my-new-app/templates/procedures/edit', ['exports'], function (exports) {

  'use strict';

  exports['default'] = Ember.HTMLBars.template((function() {
    return {
      meta: {
        "revision": "Ember@1.13.7",
        "loc": {
          "source": null,
          "start": {
            "line": 1,
            "column": 0
          },
          "end": {
            "line": 15,
            "column": 81
          }
        },
        "moduleName": "my-new-app/templates/procedures/edit.hbs"
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createElement("h1");
        dom.setAttribute(el1,"class","page-header");
        var el2 = dom.createComment("");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("div");
        dom.setAttribute(el1,"class","row");
        var el2 = dom.createTextNode("\n  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"class","col-md-6");
        var el3 = dom.createTextNode("\n    ");
        dom.appendChild(el2, el3);
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n  ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"class","col-md-6");
        var el3 = dom.createTextNode("\n    ");
        dom.appendChild(el2, el3);
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n  ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("h2");
        dom.setAttribute(el1,"class","sub-header");
        var el2 = dom.createTextNode("Acciones");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("button");
        dom.setAttribute(el1,"class","btn btn-success");
        var el2 = dom.createTextNode("Guardar");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("button");
        dom.setAttribute(el1,"class","btn btn-default");
        var el2 = dom.createTextNode("Cancelar");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("button");
        dom.setAttribute(el1,"class","btn btn-danger");
        var el2 = dom.createTextNode("Borrar");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var element0 = dom.childAt(fragment, [2]);
        var element1 = dom.childAt(fragment, [6]);
        var element2 = dom.childAt(fragment, [8]);
        var element3 = dom.childAt(fragment, [10]);
        var morphs = new Array(6);
        morphs[0] = dom.createMorphAt(dom.childAt(fragment, [0]),0,0);
        morphs[1] = dom.createMorphAt(dom.childAt(element0, [1]),1,1);
        morphs[2] = dom.createMorphAt(dom.childAt(element0, [3]),1,1);
        morphs[3] = dom.createElementMorph(element1);
        morphs[4] = dom.createElementMorph(element2);
        morphs[5] = dom.createElementMorph(element3);
        return morphs;
      },
      statements: [
        ["inline","input",[],["class","form-control","placeholder","procedure name","value",["subexpr","@mut",[["get","model.name",["loc",[null,[1,88],[1,98]]]]],[],[]]],["loc",[null,[1,24],[1,100]]]],
        ["inline","textarea",[],["class","description_edit form-control","placeholder","description","value",["subexpr","@mut",[["get","model.description",["loc",[null,[5,85],[5,102]]]]],[],[]]],["loc",[null,[5,4],[5,104]]]],
        ["inline","markdown-view",[],["value",["subexpr","@mut",[["get","model.description",["loc",[null,[8,26],[8,43]]]]],[],[]]],["loc",[null,[8,4],[8,45]]]],
        ["element","action",["saveProcedure"],[],["loc",[null,[13,32],[13,59]]]],
        ["element","action",["cancelEdition"],[],["loc",[null,[14,32],[14,59]]]],
        ["element","action",["deleteProcedure",["get","model",["loc",[null,[15,58],[15,63]]]]],[],["loc",[null,[15,31],[15,65]]]]
      ],
      locals: [],
      templates: []
    };
  }()));

});
define('my-new-app/templates/procedures/view', ['exports'], function (exports) {

  'use strict';

  exports['default'] = Ember.HTMLBars.template((function() {
    return {
      meta: {
        "revision": "Ember@1.13.7",
        "loc": {
          "source": null,
          "start": {
            "line": 1,
            "column": 0
          },
          "end": {
            "line": 10,
            "column": 0
          }
        },
        "moduleName": "my-new-app/templates/procedures/view.hbs"
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createElement("h1");
        dom.setAttribute(el1,"class","page-header");
        var el2 = dom.createComment("");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("div");
        dom.setAttribute(el1,"class","container-fluid");
        var el2 = dom.createTextNode("\n  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createComment("");
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("h2");
        dom.setAttribute(el1,"class","sub-header");
        var el2 = dom.createTextNode("Acciones");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("button");
        dom.setAttribute(el1,"class","btn btn-default");
        var el2 = dom.createTextNode("Editar");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("button");
        dom.setAttribute(el1,"class","btn btn-danger");
        var el2 = dom.createTextNode("Borrar");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var element0 = dom.childAt(fragment, [0]);
        var element1 = dom.childAt(fragment, [2]);
        var element2 = dom.childAt(fragment, [6]);
        var element3 = dom.childAt(fragment, [8]);
        var morphs = new Array(6);
        morphs[0] = dom.createElementMorph(element0);
        morphs[1] = dom.createMorphAt(element0,0,0);
        morphs[2] = dom.createElementMorph(element1);
        morphs[3] = dom.createMorphAt(element1,1,1);
        morphs[4] = dom.createElementMorph(element2);
        morphs[5] = dom.createElementMorph(element3);
        return morphs;
      },
      statements: [
        ["element","action",["editProcedure",["get","model",["loc",[null,[1,49],[1,54]]]]],["on","doubleClick"],["loc",[null,[1,24],[1,73]]]],
        ["content","model.name",["loc",[null,[1,75],[1,89]]]],
        ["element","action",["editProcedure",["get","model",["loc",[null,[3,54],[3,59]]]]],["on","doubleClick"],["loc",[null,[3,29],[3,78]]]],
        ["inline","markdown-view",[],["value",["subexpr","@mut",[["get","model.description",["loc",[null,[4,24],[4,41]]]]],[],[]]],["loc",[null,[4,2],[4,44]]]],
        ["element","action",["editProcedure",["get","model",["loc",[null,[8,57],[8,62]]]]],[],["loc",[null,[8,32],[8,64]]]],
        ["element","action",["deleteProcedure",["get","model",["loc",[null,[9,58],[9,63]]]]],[],["loc",[null,[9,31],[9,65]]]]
      ],
      locals: [],
      templates: []
    };
  }()));

});
define('my-new-app/templates/procedures', ['exports'], function (exports) {

  'use strict';

  exports['default'] = Ember.HTMLBars.template((function() {
    var child0 = (function() {
      var child0 = (function() {
        return {
          meta: {
            "revision": "Ember@1.13.7",
            "loc": {
              "source": null,
              "start": {
                "line": 12,
                "column": 12
              },
              "end": {
                "line": 12,
                "column": 70
              }
            },
            "moduleName": "my-new-app/templates/procedures.hbs"
          },
          arity: 0,
          cachedFragment: null,
          hasRendered: false,
          buildFragment: function buildFragment(dom) {
            var el0 = dom.createDocumentFragment();
            var el1 = dom.createComment("");
            dom.appendChild(el0, el1);
            return el0;
          },
          buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
            var morphs = new Array(1);
            morphs[0] = dom.createMorphAt(fragment,0,0,contextualElement);
            dom.insertBoundary(fragment, 0);
            dom.insertBoundary(fragment, null);
            return morphs;
          },
          statements: [
            ["content","procedure.name",["loc",[null,[12,52],[12,70]]]]
          ],
          locals: [],
          templates: []
        };
      }());
      return {
        meta: {
          "revision": "Ember@1.13.7",
          "loc": {
            "source": null,
            "start": {
              "line": 11,
              "column": 6
            },
            "end": {
              "line": 13,
              "column": 6
            }
          },
          "moduleName": "my-new-app/templates/procedures.hbs"
        },
        arity: 1,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode("        ");
          dom.appendChild(el0, el1);
          var el1 = dom.createElement("li");
          var el2 = dom.createComment("");
          dom.appendChild(el1, el2);
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode("\n");
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
          var morphs = new Array(1);
          morphs[0] = dom.createMorphAt(dom.childAt(fragment, [1]),0,0);
          return morphs;
        },
        statements: [
          ["block","link-to",["procedures.view",["get","procedure",["loc",[null,[12,41],[12,50]]]]],[],0,null,["loc",[null,[12,12],[12,82]]]]
        ],
        locals: ["procedure"],
        templates: [child0]
      };
    }());
    var child1 = (function() {
      return {
        meta: {
          "revision": "Ember@1.13.7",
          "loc": {
            "source": null,
            "start": {
              "line": 13,
              "column": 6
            },
            "end": {
              "line": 15,
              "column": 6
            }
          },
          "moduleName": "my-new-app/templates/procedures.hbs"
        },
        arity: 0,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode("        ");
          dom.appendChild(el0, el1);
          var el1 = dom.createElement("li");
          var el2 = dom.createTextNode("No procedures found");
          dom.appendChild(el1, el2);
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode("\n");
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes() { return []; },
        statements: [

        ],
        locals: [],
        templates: []
      };
    }());
    return {
      meta: {
        "revision": "Ember@1.13.7",
        "loc": {
          "source": null,
          "start": {
            "line": 1,
            "column": 0
          },
          "end": {
            "line": 24,
            "column": 6
          }
        },
        "moduleName": "my-new-app/templates/procedures.hbs"
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createComment("");
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("div");
        dom.setAttribute(el1,"class","row");
        var el2 = dom.createTextNode("\n\n  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createComment(" Procedure list ");
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"class","col-sm-3 col-md-2 sidebar");
        var el3 = dom.createTextNode("\n    ");
        dom.appendChild(el2, el3);
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n    ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("hr");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n    ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("button");
        dom.setAttribute(el3,"class","btn btn-success");
        var el4 = dom.createTextNode("Crear nuevo");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n    ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("ul");
        dom.setAttribute(el3,"class","nav nav-sidebar");
        var el4 = dom.createTextNode("\n");
        dom.appendChild(el3, el4);
        var el4 = dom.createComment("");
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("    ");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n  ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n\n  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createComment(" Procedure content ");
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"class","col-sm-9 col-sm-offset-3 col-md-10 col-md-offset-2 main");
        var el3 = dom.createTextNode("\n    ");
        dom.appendChild(el2, el3);
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n  ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n\n");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var element0 = dom.childAt(fragment, [2]);
        var element1 = dom.childAt(element0, [3]);
        var element2 = dom.childAt(element1, [5]);
        var morphs = new Array(5);
        morphs[0] = dom.createMorphAt(fragment,0,0,contextualElement);
        morphs[1] = dom.createMorphAt(element1,1,1);
        morphs[2] = dom.createElementMorph(element2);
        morphs[3] = dom.createMorphAt(dom.childAt(element1, [7]),1,1);
        morphs[4] = dom.createMorphAt(dom.childAt(element0, [7]),1,1);
        dom.insertBoundary(fragment, 0);
        return morphs;
      },
      statements: [
        ["content","navigation-bar",["loc",[null,[1,0],[1,18]]]],
        ["inline","input",[],["class","form-control","placeholder","Buscar...","value",["subexpr","@mut",[["get","nameOrDescription",["loc",[null,[7,63],[7,80]]]]],[],[]]],["loc",[null,[7,4],[7,83]]]],
        ["element","action",["createProcedure"],[],["loc",[null,[9,36],[9,64]]]],
        ["block","each",[["get","filteredProcedures",["loc",[null,[11,14],[11,32]]]]],[],0,1,["loc",[null,[11,6],[15,15]]]],
        ["content","outlet",["loc",[null,[21,4],[21,14]]]]
      ],
      locals: [],
      templates: [child0, child1]
    };
  }()));

});
define('my-new-app/templates/users/edit', ['exports'], function (exports) {

  'use strict';

  exports['default'] = Ember.HTMLBars.template((function() {
    return {
      meta: {
        "revision": "Ember@1.13.7",
        "loc": {
          "source": null,
          "start": {
            "line": 1,
            "column": 0
          },
          "end": {
            "line": 14,
            "column": 0
          }
        },
        "moduleName": "my-new-app/templates/users/edit.hbs"
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createComment("");
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createComment("");
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createComment("");
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createComment("");
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createComment("");
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createComment("");
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("p");
        var el2 = dom.createTextNode("\n");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("p");
        dom.setAttribute(el2,"class","property-input");
        var el3 = dom.createTextNode("\n  ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("button");
        dom.setAttribute(el3,"class","btn btn-success");
        var el4 = dom.createTextNode("Guardar");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n  ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("button");
        dom.setAttribute(el3,"class","btn btn-danger");
        var el4 = dom.createTextNode("Borrar");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var element0 = dom.childAt(fragment, [12, 1]);
        var element1 = dom.childAt(element0, [1]);
        var element2 = dom.childAt(element0, [3]);
        var morphs = new Array(8);
        morphs[0] = dom.createMorphAt(fragment,0,0,contextualElement);
        morphs[1] = dom.createMorphAt(fragment,2,2,contextualElement);
        morphs[2] = dom.createMorphAt(fragment,4,4,contextualElement);
        morphs[3] = dom.createMorphAt(fragment,6,6,contextualElement);
        morphs[4] = dom.createMorphAt(fragment,8,8,contextualElement);
        morphs[5] = dom.createMorphAt(fragment,10,10,contextualElement);
        morphs[6] = dom.createElementMorph(element1);
        morphs[7] = dom.createElementMorph(element2);
        dom.insertBoundary(fragment, 0);
        return morphs;
      },
      statements: [
        ["inline","labeled-label",[],["label","Id:","value",["subexpr","@mut",[["get","model.id",["loc",[null,[1,34],[1,42]]]]],[],[]]],["loc",[null,[1,0],[1,44]]]],
        ["inline","input",[],["class","form-control","placeholder","nombre","value",["subexpr","@mut",[["get","model.name",["loc",[null,[2,56],[2,66]]]]],[],[]]],["loc",[null,[2,0],[2,68]]]],
        ["inline","input",[],["class","form-control","placeholder","login name","value",["subexpr","@mut",[["get","model.login",["loc",[null,[3,60],[3,71]]]]],[],[]]],["loc",[null,[3,0],[3,73]]]],
        ["inline","input",[],["class","form-control","placeholder","password","value",["subexpr","@mut",[["get","model.password",["loc",[null,[4,58],[4,72]]]]],[],[]]],["loc",[null,[4,0],[4,74]]]],
        ["inline","labeled-label",[],["label","Created:","value",["subexpr","@mut",[["get","model.creation",["loc",[null,[5,39],[5,53]]]]],[],[]]],["loc",[null,[5,0],[5,55]]]],
        ["inline","labeled-label",[],["label","Edited:","value",["subexpr","@mut",[["get","model.modification",["loc",[null,[6,38],[6,56]]]]],[],[]]],["loc",[null,[6,0],[6,58]]]],
        ["element","action",["save"],[],["loc",[null,[10,34],[10,51]]]],
        ["element","action",["remove"],[],["loc",[null,[11,33],[11,52]]]]
      ],
      locals: [],
      templates: []
    };
  }()));

});
define('my-new-app/templates/users', ['exports'], function (exports) {

  'use strict';

  exports['default'] = Ember.HTMLBars.template((function() {
    var child0 = (function() {
      var child0 = (function() {
        return {
          meta: {
            "revision": "Ember@1.13.7",
            "loc": {
              "source": null,
              "start": {
                "line": 24,
                "column": 16
              },
              "end": {
                "line": 24,
                "column": 57
              }
            },
            "moduleName": "my-new-app/templates/users.hbs"
          },
          arity: 0,
          cachedFragment: null,
          hasRendered: false,
          buildFragment: function buildFragment(dom) {
            var el0 = dom.createDocumentFragment();
            var el1 = dom.createComment("");
            dom.appendChild(el0, el1);
            return el0;
          },
          buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
            var morphs = new Array(1);
            morphs[0] = dom.createMorphAt(fragment,0,0,contextualElement);
            dom.insertBoundary(fragment, 0);
            dom.insertBoundary(fragment, null);
            return morphs;
          },
          statements: [
            ["content","user.id",["loc",[null,[24,46],[24,57]]]]
          ],
          locals: [],
          templates: []
        };
      }());
      var child1 = (function() {
        return {
          meta: {
            "revision": "Ember@1.13.7",
            "loc": {
              "source": null,
              "start": {
                "line": 25,
                "column": 16
              },
              "end": {
                "line": 25,
                "column": 59
              }
            },
            "moduleName": "my-new-app/templates/users.hbs"
          },
          arity: 0,
          cachedFragment: null,
          hasRendered: false,
          buildFragment: function buildFragment(dom) {
            var el0 = dom.createDocumentFragment();
            var el1 = dom.createComment("");
            dom.appendChild(el0, el1);
            return el0;
          },
          buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
            var morphs = new Array(1);
            morphs[0] = dom.createMorphAt(fragment,0,0,contextualElement);
            dom.insertBoundary(fragment, 0);
            dom.insertBoundary(fragment, null);
            return morphs;
          },
          statements: [
            ["content","user.name",["loc",[null,[25,46],[25,59]]]]
          ],
          locals: [],
          templates: []
        };
      }());
      return {
        meta: {
          "revision": "Ember@1.13.7",
          "loc": {
            "source": null,
            "start": {
              "line": 22,
              "column": 8
            },
            "end": {
              "line": 29,
              "column": 8
            }
          },
          "moduleName": "my-new-app/templates/users.hbs"
        },
        arity: 1,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode("          ");
          dom.appendChild(el0, el1);
          var el1 = dom.createElement("tr");
          var el2 = dom.createTextNode("\n            ");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("td");
          var el3 = dom.createComment("");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n            ");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("td");
          var el3 = dom.createComment("");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n            ");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("td");
          var el3 = dom.createComment("");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n            ");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("td");
          var el3 = dom.createComment("");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n          ");
          dom.appendChild(el1, el2);
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode("\n");
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
          var element0 = dom.childAt(fragment, [1]);
          var morphs = new Array(4);
          morphs[0] = dom.createMorphAt(dom.childAt(element0, [1]),0,0);
          morphs[1] = dom.createMorphAt(dom.childAt(element0, [3]),0,0);
          morphs[2] = dom.createMorphAt(dom.childAt(element0, [5]),0,0);
          morphs[3] = dom.createMorphAt(dom.childAt(element0, [7]),0,0);
          return morphs;
        },
        statements: [
          ["block","link-to",["users.edit",["get","user",["loc",[null,[24,40],[24,44]]]]],[],0,null,["loc",[null,[24,16],[24,69]]]],
          ["block","link-to",["users.edit",["get","user",["loc",[null,[25,40],[25,44]]]]],[],1,null,["loc",[null,[25,16],[25,71]]]],
          ["content","user.login",["loc",[null,[26,16],[26,30]]]],
          ["content","user.password",["loc",[null,[27,16],[27,33]]]]
        ],
        locals: ["user"],
        templates: [child0, child1]
      };
    }());
    var child1 = (function() {
      return {
        meta: {
          "revision": "Ember@1.13.7",
          "loc": {
            "source": null,
            "start": {
              "line": 29,
              "column": 8
            },
            "end": {
              "line": 33,
              "column": 8
            }
          },
          "moduleName": "my-new-app/templates/users.hbs"
        },
        arity: 0,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode("          ");
          dom.appendChild(el0, el1);
          var el1 = dom.createElement("tr");
          var el2 = dom.createTextNode("\n            ");
          dom.appendChild(el1, el2);
          var el2 = dom.createElement("td");
          dom.setAttribute(el2,"colspan","4");
          var el3 = dom.createTextNode("No users found");
          dom.appendChild(el2, el3);
          dom.appendChild(el1, el2);
          var el2 = dom.createTextNode("\n          ");
          dom.appendChild(el1, el2);
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode("\n");
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes() { return []; },
        statements: [

        ],
        locals: [],
        templates: []
      };
    }());
    return {
      meta: {
        "revision": "Ember@1.13.7",
        "loc": {
          "source": null,
          "start": {
            "line": 1,
            "column": 0
          },
          "end": {
            "line": 54,
            "column": 0
          }
        },
        "moduleName": "my-new-app/templates/users.hbs"
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createComment("");
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("div");
        dom.setAttribute(el1,"class","row");
        var el2 = dom.createTextNode("\n\n  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createComment(" User list ");
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"class","col-md-8");
        var el3 = dom.createTextNode("\n    ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("button");
        dom.setAttribute(el3,"class","btn btn-success");
        var el4 = dom.createTextNode("Crear Nuevo");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n\n    ");
        dom.appendChild(el2, el3);
        var el3 = dom.createElement("div");
        dom.setAttribute(el3,"class","table-responsive");
        var el4 = dom.createTextNode("\n      ");
        dom.appendChild(el3, el4);
        var el4 = dom.createElement("table");
        dom.setAttribute(el4,"class","table table-striped");
        var el5 = dom.createTextNode("\n        ");
        dom.appendChild(el4, el5);
        var el5 = dom.createElement("thead");
        var el6 = dom.createTextNode("\n        ");
        dom.appendChild(el5, el6);
        var el6 = dom.createElement("tr");
        var el7 = dom.createTextNode("\n\n          ");
        dom.appendChild(el6, el7);
        var el7 = dom.createElement("th");
        var el8 = dom.createTextNode("Id");
        dom.appendChild(el7, el8);
        dom.appendChild(el6, el7);
        var el7 = dom.createTextNode("\n          ");
        dom.appendChild(el6, el7);
        var el7 = dom.createElement("th");
        var el8 = dom.createTextNode("Nombre");
        dom.appendChild(el7, el8);
        dom.appendChild(el6, el7);
        var el7 = dom.createTextNode("\n          ");
        dom.appendChild(el6, el7);
        var el7 = dom.createElement("th");
        var el8 = dom.createTextNode("Login");
        dom.appendChild(el7, el8);
        dom.appendChild(el6, el7);
        var el7 = dom.createTextNode("\n          ");
        dom.appendChild(el6, el7);
        var el7 = dom.createElement("th");
        var el8 = dom.createTextNode("Password");
        dom.appendChild(el7, el8);
        dom.appendChild(el6, el7);
        var el7 = dom.createTextNode("\n        ");
        dom.appendChild(el6, el7);
        dom.appendChild(el5, el6);
        var el6 = dom.createTextNode("\n        ");
        dom.appendChild(el5, el6);
        dom.appendChild(el4, el5);
        var el5 = dom.createTextNode("\n\n        ");
        dom.appendChild(el4, el5);
        var el5 = dom.createElement("tbody");
        var el6 = dom.createTextNode("\n");
        dom.appendChild(el5, el6);
        var el6 = dom.createComment("");
        dom.appendChild(el5, el6);
        var el6 = dom.createTextNode("        ");
        dom.appendChild(el5, el6);
        dom.appendChild(el4, el5);
        var el5 = dom.createTextNode("\n\n        ");
        dom.appendChild(el4, el5);
        var el5 = dom.createElement("tfoot");
        var el6 = dom.createTextNode("\n        ");
        dom.appendChild(el5, el6);
        var el6 = dom.createElement("tr");
        var el7 = dom.createTextNode("\n          ");
        dom.appendChild(el6, el7);
        var el7 = dom.createElement("td");
        var el8 = dom.createTextNode("Total");
        dom.appendChild(el7, el8);
        dom.appendChild(el6, el7);
        var el7 = dom.createTextNode("\n          ");
        dom.appendChild(el6, el7);
        var el7 = dom.createElement("td");
        dom.appendChild(el6, el7);
        var el7 = dom.createTextNode("\n          ");
        dom.appendChild(el6, el7);
        var el7 = dom.createElement("td");
        dom.appendChild(el6, el7);
        var el7 = dom.createTextNode("\n          ");
        dom.appendChild(el6, el7);
        var el7 = dom.createElement("td");
        var el8 = dom.createComment("");
        dom.appendChild(el7, el8);
        dom.appendChild(el6, el7);
        var el7 = dom.createTextNode("\n        ");
        dom.appendChild(el6, el7);
        dom.appendChild(el5, el6);
        var el6 = dom.createTextNode("\n        ");
        dom.appendChild(el5, el6);
        dom.appendChild(el4, el5);
        var el5 = dom.createTextNode("\n      ");
        dom.appendChild(el4, el5);
        dom.appendChild(el3, el4);
        var el4 = dom.createTextNode("\n    ");
        dom.appendChild(el3, el4);
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n  ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n\n  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createComment(" User edit form ");
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n  ");
        dom.appendChild(el1, el2);
        var el2 = dom.createElement("div");
        dom.setAttribute(el2,"class","col-md-4");
        var el3 = dom.createTextNode("\n    ");
        dom.appendChild(el2, el3);
        var el3 = dom.createComment("");
        dom.appendChild(el2, el3);
        var el3 = dom.createTextNode("\n  ");
        dom.appendChild(el2, el3);
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n\n");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var element1 = dom.childAt(fragment, [2]);
        var element2 = dom.childAt(element1, [3]);
        var element3 = dom.childAt(element2, [1]);
        var element4 = dom.childAt(element2, [3, 1]);
        var morphs = new Array(5);
        morphs[0] = dom.createMorphAt(fragment,0,0,contextualElement);
        morphs[1] = dom.createElementMorph(element3);
        morphs[2] = dom.createMorphAt(dom.childAt(element4, [3]),1,1);
        morphs[3] = dom.createMorphAt(dom.childAt(element4, [5, 1, 7]),0,0);
        morphs[4] = dom.createMorphAt(dom.childAt(element1, [7]),1,1);
        dom.insertBoundary(fragment, 0);
        return morphs;
      },
      statements: [
        ["content","navigation-bar",["loc",[null,[1,0],[1,18]]]],
        ["element","action",["create"],[],["loc",[null,[7,36],[7,55]]]],
        ["block","each",[["get","model",["loc",[null,[22,16],[22,21]]]]],[],0,1,["loc",[null,[22,8],[33,17]]]],
        ["content","model.length",["loc",[null,[41,14],[41,30]]]],
        ["content","outlet",["loc",[null,[50,4],[50,14]]]]
      ],
      locals: [],
      templates: [child0, child1]
    };
  }()));

});
define('my-new-app/templates/wrong-paths', ['exports'], function (exports) {

  'use strict';

  exports['default'] = Ember.HTMLBars.template((function() {
    return {
      meta: {
        "revision": "Ember@1.13.7",
        "loc": {
          "source": null,
          "start": {
            "line": 1,
            "column": 0
          },
          "end": {
            "line": 4,
            "column": 7
          }
        },
        "moduleName": "my-new-app/templates/wrong-paths.hbs"
      },
      arity: 0,
      cachedFragment: null,
      hasRendered: false,
      buildFragment: function buildFragment(dom) {
        var el0 = dom.createDocumentFragment();
        var el1 = dom.createComment(" User list grid ");
        dom.appendChild(el0, el1);
        var el1 = dom.createTextNode("\n");
        dom.appendChild(el0, el1);
        var el1 = dom.createElement("span");
        var el2 = dom.createTextNode("\n  Wrong URL! There's no route: ");
        dom.appendChild(el1, el2);
        var el2 = dom.createComment("");
        dom.appendChild(el1, el2);
        var el2 = dom.createTextNode("\n");
        dom.appendChild(el1, el2);
        dom.appendChild(el0, el1);
        return el0;
      },
      buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
        var morphs = new Array(1);
        morphs[0] = dom.createMorphAt(dom.childAt(fragment, [2]),1,1);
        return morphs;
      },
      statements: [
        ["content","model.wrong-paths",["loc",[null,[3,31],[3,52]]]]
      ],
      locals: [],
      templates: []
    };
  }()));

});
define('my-new-app/tests/adapters/application.jshint', function () {

  'use strict';

  QUnit.module('JSHint - adapters');
  QUnit.test('adapters/application.js should pass jshint', function(assert) { 
    assert.ok(false, 'adapters/application.js should pass jshint.\nadapters/application.js: line 1, col 8, \'Ember\' is defined but never used.\nadapters/application.js: line 3, col 8, \'loginRoute\' is defined but never used.\n\n2 errors'); 
  });

});
define('my-new-app/tests/app.jshint', function () {

  'use strict';

  QUnit.module('JSHint - .');
  QUnit.test('app.js should pass jshint', function(assert) { 
    assert.ok(true, 'app.js should pass jshint.'); 
  });

});
define('my-new-app/tests/components/labeled-label.jshint', function () {

  'use strict';

  QUnit.module('JSHint - components');
  QUnit.test('components/labeled-label.js should pass jshint', function(assert) { 
    assert.ok(true, 'components/labeled-label.js should pass jshint.'); 
  });

});
define('my-new-app/tests/components/markdown-view.jshint', function () {

  'use strict';

  QUnit.module('JSHint - components');
  QUnit.test('components/markdown-view.js should pass jshint', function(assert) { 
    assert.ok(true, 'components/markdown-view.js should pass jshint.'); 
  });

});
define('my-new-app/tests/components/navigation-bar.jshint', function () {

  'use strict';

  QUnit.module('JSHint - components');
  QUnit.test('components/navigation-bar.js should pass jshint', function(assert) { 
    assert.ok(true, 'components/navigation-bar.js should pass jshint.'); 
  });

});
define('my-new-app/tests/controllers/login.jshint', function () {

  'use strict';

  QUnit.module('JSHint - controllers');
  QUnit.test('controllers/login.js should pass jshint', function(assert) { 
    assert.ok(true, 'controllers/login.js should pass jshint.'); 
  });

});
define('my-new-app/tests/controllers/procedures/edit.jshint', function () {

  'use strict';

  QUnit.module('JSHint - controllers/procedures');
  QUnit.test('controllers/procedures/edit.js should pass jshint', function(assert) { 
    assert.ok(true, 'controllers/procedures/edit.js should pass jshint.'); 
  });

});
define('my-new-app/tests/controllers/procedures/view.jshint', function () {

  'use strict';

  QUnit.module('JSHint - controllers/procedures');
  QUnit.test('controllers/procedures/view.js should pass jshint', function(assert) { 
    assert.ok(true, 'controllers/procedures/view.js should pass jshint.'); 
  });

});
define('my-new-app/tests/controllers/procedures.jshint', function () {

  'use strict';

  QUnit.module('JSHint - controllers');
  QUnit.test('controllers/procedures.js should pass jshint', function(assert) { 
    assert.ok(true, 'controllers/procedures.js should pass jshint.'); 
  });

});
define('my-new-app/tests/controllers/users/edit.jshint', function () {

  'use strict';

  QUnit.module('JSHint - controllers/users');
  QUnit.test('controllers/users/edit.js should pass jshint', function(assert) { 
    assert.ok(true, 'controllers/users/edit.js should pass jshint.'); 
  });

});
define('my-new-app/tests/controllers/users.jshint', function () {

  'use strict';

  QUnit.module('JSHint - controllers');
  QUnit.test('controllers/users.js should pass jshint', function(assert) { 
    assert.ok(true, 'controllers/users.js should pass jshint.'); 
  });

});
define('my-new-app/tests/helpers/markdown-ashtml.jshint', function () {

  'use strict';

  QUnit.module('JSHint - helpers');
  QUnit.test('helpers/markdown-ashtml.js should pass jshint', function(assert) { 
    assert.ok(false, 'helpers/markdown-ashtml.js should pass jshint.\nhelpers/markdown-ashtml.js: line 10, col 42, \'marked\' is not defined.\nhelpers/markdown-ashtml.js: line 2, col 1, \'moment\' is defined but never used.\n\n2 errors'); 
  });

});
define('my-new-app/tests/helpers/resolver', ['exports', 'ember/resolver', 'my-new-app/config/environment'], function (exports, Resolver, config) {

  'use strict';

  var resolver = Resolver['default'].create();

  resolver.namespace = {
    modulePrefix: config['default'].modulePrefix,
    podModulePrefix: config['default'].podModulePrefix
  };

  exports['default'] = resolver;

});
define('my-new-app/tests/helpers/resolver.jshint', function () {

  'use strict';

  QUnit.module('JSHint - helpers');
  QUnit.test('helpers/resolver.js should pass jshint', function(assert) { 
    assert.ok(true, 'helpers/resolver.js should pass jshint.'); 
  });

});
define('my-new-app/tests/helpers/start-app', ['exports', 'ember', 'my-new-app/app', 'my-new-app/config/environment'], function (exports, Ember, Application, config) {

  'use strict';



  exports['default'] = startApp;
  function startApp(attrs) {
    var application;

    var attributes = Ember['default'].merge({}, config['default'].APP);
    attributes = Ember['default'].merge(attributes, attrs); // use defaults, but you can override;

    Ember['default'].run(function () {
      application = Application['default'].create(attributes);
      application.setupForTesting();
      application.injectTestHelpers();
    });

    return application;
  }

});
define('my-new-app/tests/helpers/start-app.jshint', function () {

  'use strict';

  QUnit.module('JSHint - helpers');
  QUnit.test('helpers/start-app.js should pass jshint', function(assert) { 
    assert.ok(true, 'helpers/start-app.js should pass jshint.'); 
  });

});
define('my-new-app/tests/integration/components/labeled-label-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleForComponent('labeled-label', 'Integration | Component | labeled label', {
    integration: true
  });

  ember_qunit.test('it renders', function (assert) {
    assert.expect(2);

    // Set any properties with this.set('myProperty', 'value');
    // Handle any actions with this.on('myAction', function(val) { ... });

    this.render(Ember.HTMLBars.template((function () {
      return {
        meta: {
          'revision': 'Ember@1.13.7',
          'loc': {
            'source': null,
            'start': {
              'line': 1,
              'column': 0
            },
            'end': {
              'line': 1,
              'column': 17
            }
          }
        },
        arity: 0,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createComment('');
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
          var morphs = new Array(1);
          morphs[0] = dom.createMorphAt(fragment, 0, 0, contextualElement);
          dom.insertBoundary(fragment, 0);
          dom.insertBoundary(fragment, null);
          return morphs;
        },
        statements: [['content', 'labeled-label', ['loc', [null, [1, 0], [1, 17]]]]],
        locals: [],
        templates: []
      };
    })()));

    assert.equal(this.$().text().trim(), '');

    // Template block usage:
    this.render(Ember.HTMLBars.template((function () {
      var child0 = (function () {
        return {
          meta: {
            'revision': 'Ember@1.13.7',
            'loc': {
              'source': null,
              'start': {
                'line': 2,
                'column': 4
              },
              'end': {
                'line': 4,
                'column': 4
              }
            }
          },
          arity: 0,
          cachedFragment: null,
          hasRendered: false,
          buildFragment: function buildFragment(dom) {
            var el0 = dom.createDocumentFragment();
            var el1 = dom.createTextNode('      template block text\n');
            dom.appendChild(el0, el1);
            return el0;
          },
          buildRenderNodes: function buildRenderNodes() {
            return [];
          },
          statements: [],
          locals: [],
          templates: []
        };
      })();

      return {
        meta: {
          'revision': 'Ember@1.13.7',
          'loc': {
            'source': null,
            'start': {
              'line': 1,
              'column': 0
            },
            'end': {
              'line': 5,
              'column': 2
            }
          }
        },
        arity: 0,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode('\n');
          dom.appendChild(el0, el1);
          var el1 = dom.createComment('');
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode('  ');
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
          var morphs = new Array(1);
          morphs[0] = dom.createMorphAt(fragment, 1, 1, contextualElement);
          return morphs;
        },
        statements: [['block', 'labeled-label', [], [], 0, null, ['loc', [null, [2, 4], [4, 22]]]]],
        locals: [],
        templates: [child0]
      };
    })()));

    assert.equal(this.$().text().trim(), 'template block text');
  });

});
define('my-new-app/tests/integration/components/labeled-label-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - integration/components');
  QUnit.test('integration/components/labeled-label-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'integration/components/labeled-label-test.js should pass jshint.'); 
  });

});
define('my-new-app/tests/integration/components/markdown-view-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleForComponent('markdown-view', 'Integration | Component | markdown view', {
    integration: true
  });

  ember_qunit.test('it renders', function (assert) {
    assert.expect(2);

    // Set any properties with this.set('myProperty', 'value');
    // Handle any actions with this.on('myAction', function(val) { ... });

    this.render(Ember.HTMLBars.template((function () {
      return {
        meta: {
          'revision': 'Ember@1.13.7',
          'loc': {
            'source': null,
            'start': {
              'line': 1,
              'column': 0
            },
            'end': {
              'line': 1,
              'column': 17
            }
          }
        },
        arity: 0,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createComment('');
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
          var morphs = new Array(1);
          morphs[0] = dom.createMorphAt(fragment, 0, 0, contextualElement);
          dom.insertBoundary(fragment, 0);
          dom.insertBoundary(fragment, null);
          return morphs;
        },
        statements: [['content', 'markdown-view', ['loc', [null, [1, 0], [1, 17]]]]],
        locals: [],
        templates: []
      };
    })()));

    assert.equal(this.$().text().trim(), '');

    // Template block usage:
    this.render(Ember.HTMLBars.template((function () {
      var child0 = (function () {
        return {
          meta: {
            'revision': 'Ember@1.13.7',
            'loc': {
              'source': null,
              'start': {
                'line': 2,
                'column': 4
              },
              'end': {
                'line': 4,
                'column': 4
              }
            }
          },
          arity: 0,
          cachedFragment: null,
          hasRendered: false,
          buildFragment: function buildFragment(dom) {
            var el0 = dom.createDocumentFragment();
            var el1 = dom.createTextNode('      template block text\n');
            dom.appendChild(el0, el1);
            return el0;
          },
          buildRenderNodes: function buildRenderNodes() {
            return [];
          },
          statements: [],
          locals: [],
          templates: []
        };
      })();

      return {
        meta: {
          'revision': 'Ember@1.13.7',
          'loc': {
            'source': null,
            'start': {
              'line': 1,
              'column': 0
            },
            'end': {
              'line': 5,
              'column': 2
            }
          }
        },
        arity: 0,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode('\n');
          dom.appendChild(el0, el1);
          var el1 = dom.createComment('');
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode('  ');
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
          var morphs = new Array(1);
          morphs[0] = dom.createMorphAt(fragment, 1, 1, contextualElement);
          return morphs;
        },
        statements: [['block', 'markdown-view', [], [], 0, null, ['loc', [null, [2, 4], [4, 22]]]]],
        locals: [],
        templates: [child0]
      };
    })()));

    assert.equal(this.$().text().trim(), 'template block text');
  });

});
define('my-new-app/tests/integration/components/markdown-view-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - integration/components');
  QUnit.test('integration/components/markdown-view-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'integration/components/markdown-view-test.js should pass jshint.'); 
  });

});
define('my-new-app/tests/integration/components/navigation-bar-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleForComponent('navigation-bar', 'Integration | Component | navigation bar', {
    integration: true
  });

  ember_qunit.test('it renders', function (assert) {
    assert.expect(2);

    // Set any properties with this.set('myProperty', 'value');
    // Handle any actions with this.on('myAction', function(val) { ... });

    this.render(Ember.HTMLBars.template((function () {
      return {
        meta: {
          'revision': 'Ember@1.13.7',
          'loc': {
            'source': null,
            'start': {
              'line': 1,
              'column': 0
            },
            'end': {
              'line': 1,
              'column': 18
            }
          }
        },
        arity: 0,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createComment('');
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
          var morphs = new Array(1);
          morphs[0] = dom.createMorphAt(fragment, 0, 0, contextualElement);
          dom.insertBoundary(fragment, 0);
          dom.insertBoundary(fragment, null);
          return morphs;
        },
        statements: [['content', 'navigation-bar', ['loc', [null, [1, 0], [1, 18]]]]],
        locals: [],
        templates: []
      };
    })()));

    assert.equal(this.$().text().trim(), '');

    // Template block usage:
    this.render(Ember.HTMLBars.template((function () {
      var child0 = (function () {
        return {
          meta: {
            'revision': 'Ember@1.13.7',
            'loc': {
              'source': null,
              'start': {
                'line': 2,
                'column': 4
              },
              'end': {
                'line': 4,
                'column': 4
              }
            }
          },
          arity: 0,
          cachedFragment: null,
          hasRendered: false,
          buildFragment: function buildFragment(dom) {
            var el0 = dom.createDocumentFragment();
            var el1 = dom.createTextNode('      template block text\n');
            dom.appendChild(el0, el1);
            return el0;
          },
          buildRenderNodes: function buildRenderNodes() {
            return [];
          },
          statements: [],
          locals: [],
          templates: []
        };
      })();

      return {
        meta: {
          'revision': 'Ember@1.13.7',
          'loc': {
            'source': null,
            'start': {
              'line': 1,
              'column': 0
            },
            'end': {
              'line': 5,
              'column': 2
            }
          }
        },
        arity: 0,
        cachedFragment: null,
        hasRendered: false,
        buildFragment: function buildFragment(dom) {
          var el0 = dom.createDocumentFragment();
          var el1 = dom.createTextNode('\n');
          dom.appendChild(el0, el1);
          var el1 = dom.createComment('');
          dom.appendChild(el0, el1);
          var el1 = dom.createTextNode('  ');
          dom.appendChild(el0, el1);
          return el0;
        },
        buildRenderNodes: function buildRenderNodes(dom, fragment, contextualElement) {
          var morphs = new Array(1);
          morphs[0] = dom.createMorphAt(fragment, 1, 1, contextualElement);
          return morphs;
        },
        statements: [['block', 'navigation-bar', [], [], 0, null, ['loc', [null, [2, 4], [4, 23]]]]],
        locals: [],
        templates: [child0]
      };
    })()));

    assert.equal(this.$().text().trim(), 'template block text');
  });

});
define('my-new-app/tests/integration/components/navigation-bar-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - integration/components');
  QUnit.test('integration/components/navigation-bar-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'integration/components/navigation-bar-test.js should pass jshint.'); 
  });

});
define('my-new-app/tests/mixins/authenticated-route.jshint', function () {

  'use strict';

  QUnit.module('JSHint - mixins');
  QUnit.test('mixins/authenticated-route.js should pass jshint', function(assert) { 
    assert.ok(true, 'mixins/authenticated-route.js should pass jshint.'); 
  });

});
define('my-new-app/tests/models/procedure.jshint', function () {

  'use strict';

  QUnit.module('JSHint - models');
  QUnit.test('models/procedure.js should pass jshint', function(assert) { 
    assert.ok(true, 'models/procedure.js should pass jshint.'); 
  });

});
define('my-new-app/tests/models/user.jshint', function () {

  'use strict';

  QUnit.module('JSHint - models');
  QUnit.test('models/user.js should pass jshint', function(assert) { 
    assert.ok(true, 'models/user.js should pass jshint.'); 
  });

});
define('my-new-app/tests/router.jshint', function () {

  'use strict';

  QUnit.module('JSHint - .');
  QUnit.test('router.js should pass jshint', function(assert) { 
    assert.ok(true, 'router.js should pass jshint.'); 
  });

});
define('my-new-app/tests/routes/application.jshint', function () {

  'use strict';

  QUnit.module('JSHint - routes');
  QUnit.test('routes/application.js should pass jshint', function(assert) { 
    assert.ok(false, 'routes/application.js should pass jshint.\nroutes/application.js: line 28, col 18, \'response\' is defined but never used.\n\n1 error'); 
  });

});
define('my-new-app/tests/routes/index.jshint', function () {

  'use strict';

  QUnit.module('JSHint - routes');
  QUnit.test('routes/index.js should pass jshint', function(assert) { 
    assert.ok(true, 'routes/index.js should pass jshint.'); 
  });

});
define('my-new-app/tests/routes/login.jshint', function () {

  'use strict';

  QUnit.module('JSHint - routes');
  QUnit.test('routes/login.js should pass jshint', function(assert) { 
    assert.ok(true, 'routes/login.js should pass jshint.'); 
  });

});
define('my-new-app/tests/routes/procedures/edit.jshint', function () {

  'use strict';

  QUnit.module('JSHint - routes/procedures');
  QUnit.test('routes/procedures/edit.js should pass jshint', function(assert) { 
    assert.ok(true, 'routes/procedures/edit.js should pass jshint.'); 
  });

});
define('my-new-app/tests/routes/procedures/view.jshint', function () {

  'use strict';

  QUnit.module('JSHint - routes/procedures');
  QUnit.test('routes/procedures/view.js should pass jshint', function(assert) { 
    assert.ok(true, 'routes/procedures/view.js should pass jshint.'); 
  });

});
define('my-new-app/tests/routes/procedures.jshint', function () {

  'use strict';

  QUnit.module('JSHint - routes');
  QUnit.test('routes/procedures.js should pass jshint', function(assert) { 
    assert.ok(true, 'routes/procedures.js should pass jshint.'); 
  });

});
define('my-new-app/tests/routes/users.jshint', function () {

  'use strict';

  QUnit.module('JSHint - routes');
  QUnit.test('routes/users.js should pass jshint', function(assert) { 
    assert.ok(true, 'routes/users.js should pass jshint.'); 
  });

});
define('my-new-app/tests/serializers/application.jshint', function () {

  'use strict';

  QUnit.module('JSHint - serializers');
  QUnit.test('serializers/application.js should pass jshint', function(assert) { 
    assert.ok(false, 'serializers/application.js should pass jshint.\nserializers/application.js: line 19, col 5, \'Ember\' is not defined.\n\n1 error'); 
  });

});
define('my-new-app/tests/test-helper', ['my-new-app/tests/helpers/resolver', 'ember-qunit'], function (resolver, ember_qunit) {

	'use strict';

	ember_qunit.setResolver(resolver['default']);

});
define('my-new-app/tests/test-helper.jshint', function () {

  'use strict';

  QUnit.module('JSHint - .');
  QUnit.test('test-helper.js should pass jshint', function(assert) { 
    assert.ok(true, 'test-helper.js should pass jshint.'); 
  });

});
define('my-new-app/tests/unit/adapters/application-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor('adapter:application', 'Unit | Adapter | application', {
    // Specify the other units that are required for this test.
    // needs: ['serializer:foo']
  });

  // Replace this with your real tests.
  ember_qunit.test('it exists', function (assert) {
    var adapter = this.subject();
    assert.ok(adapter);
  });

});
define('my-new-app/tests/unit/adapters/application-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - unit/adapters');
  QUnit.test('unit/adapters/application-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'unit/adapters/application-test.js should pass jshint.'); 
  });

});
define('my-new-app/tests/unit/controllers/login-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor('controller:login', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  // Replace this with your real tests.
  ember_qunit.test('it exists', function (assert) {
    var controller = this.subject();
    assert.ok(controller);
  });

});
define('my-new-app/tests/unit/controllers/login-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - unit/controllers');
  QUnit.test('unit/controllers/login-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'unit/controllers/login-test.js should pass jshint.'); 
  });

});
define('my-new-app/tests/unit/controllers/procedures/edit-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor('controller:procedures/edit', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  // Replace this with your real tests.
  ember_qunit.test('it exists', function (assert) {
    var controller = this.subject();
    assert.ok(controller);
  });

});
define('my-new-app/tests/unit/controllers/procedures/edit-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - unit/controllers/procedures');
  QUnit.test('unit/controllers/procedures/edit-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'unit/controllers/procedures/edit-test.js should pass jshint.'); 
  });

});
define('my-new-app/tests/unit/controllers/procedures/view-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor('controller:procedures/view', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  // Replace this with your real tests.
  ember_qunit.test('it exists', function (assert) {
    var controller = this.subject();
    assert.ok(controller);
  });

});
define('my-new-app/tests/unit/controllers/procedures/view-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - unit/controllers/procedures');
  QUnit.test('unit/controllers/procedures/view-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'unit/controllers/procedures/view-test.js should pass jshint.'); 
  });

});
define('my-new-app/tests/unit/controllers/procedures-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor('controller:procedures', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  // Replace this with your real tests.
  ember_qunit.test('it exists', function (assert) {
    var controller = this.subject();
    assert.ok(controller);
  });

});
define('my-new-app/tests/unit/controllers/procedures-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - unit/controllers');
  QUnit.test('unit/controllers/procedures-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'unit/controllers/procedures-test.js should pass jshint.'); 
  });

});
define('my-new-app/tests/unit/controllers/users/edit-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor('controller:users/edit', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  // Replace this with your real tests.
  ember_qunit.test('it exists', function (assert) {
    var controller = this.subject();
    assert.ok(controller);
  });

});
define('my-new-app/tests/unit/controllers/users/edit-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - unit/controllers/users');
  QUnit.test('unit/controllers/users/edit-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'unit/controllers/users/edit-test.js should pass jshint.'); 
  });

});
define('my-new-app/tests/unit/controllers/users-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor('controller:users', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  // Replace this with your real tests.
  ember_qunit.test('it exists', function (assert) {
    var controller = this.subject();
    assert.ok(controller);
  });

});
define('my-new-app/tests/unit/controllers/users-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - unit/controllers');
  QUnit.test('unit/controllers/users-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'unit/controllers/users-test.js should pass jshint.'); 
  });

});
define('my-new-app/tests/unit/helpers/markdown-ashtml-test', ['my-new-app/helpers/markdown-ashtml', 'qunit'], function (markdown_ashtml, qunit) {

  'use strict';

  qunit.module('Unit | Helper | markdown ashtml');

  // Replace this with your real tests.
  qunit.test('it works', function (assert) {
    var result = markdown_ashtml.markdownAshtml(42);
    assert.ok(result);
  });

});
define('my-new-app/tests/unit/helpers/markdown-ashtml-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - unit/helpers');
  QUnit.test('unit/helpers/markdown-ashtml-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'unit/helpers/markdown-ashtml-test.js should pass jshint.'); 
  });

});
define('my-new-app/tests/unit/mixins/authenticated-route-test', ['ember', 'my-new-app/mixins/authenticated-route', 'qunit'], function (Ember, AuthenticatedRouteMixin, qunit) {

  'use strict';

  qunit.module('Unit | Mixin | authenticated route');

  // Replace this with your real tests.
  qunit.test('it works', function (assert) {
    var AuthenticatedRouteObject = Ember['default'].Object.extend(AuthenticatedRouteMixin['default']);
    var subject = AuthenticatedRouteObject.create();
    assert.ok(subject);
  });

});
define('my-new-app/tests/unit/mixins/authenticated-route-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - unit/mixins');
  QUnit.test('unit/mixins/authenticated-route-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'unit/mixins/authenticated-route-test.js should pass jshint.'); 
  });

});
define('my-new-app/tests/unit/models/procedure-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleForModel('procedure', 'Unit | Model | procedure', {
    // Specify the other units that are required for this test.
    needs: []
  });

  ember_qunit.test('it exists', function (assert) {
    var model = this.subject();
    // var store = this.store();
    assert.ok(!!model);
  });

});
define('my-new-app/tests/unit/models/procedure-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - unit/models');
  QUnit.test('unit/models/procedure-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'unit/models/procedure-test.js should pass jshint.'); 
  });

});
define('my-new-app/tests/unit/models/user-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleForModel('user', 'Unit | Model | user', {
    // Specify the other units that are required for this test.
    needs: []
  });

  ember_qunit.test('it exists', function (assert) {
    var model = this.subject();
    // var store = this.store();
    assert.ok(!!model);
  });

});
define('my-new-app/tests/unit/models/user-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - unit/models');
  QUnit.test('unit/models/user-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'unit/models/user-test.js should pass jshint.'); 
  });

});
define('my-new-app/tests/unit/routes/application-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor('route:application', 'Unit | Route | application', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  ember_qunit.test('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });

});
define('my-new-app/tests/unit/routes/application-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - unit/routes');
  QUnit.test('unit/routes/application-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'unit/routes/application-test.js should pass jshint.'); 
  });

});
define('my-new-app/tests/unit/routes/index-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor('route:index', 'Unit | Route | index', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  ember_qunit.test('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });

});
define('my-new-app/tests/unit/routes/index-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - unit/routes');
  QUnit.test('unit/routes/index-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'unit/routes/index-test.js should pass jshint.'); 
  });

});
define('my-new-app/tests/unit/routes/login-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor('route:login', 'Unit | Route | login', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  ember_qunit.test('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });

});
define('my-new-app/tests/unit/routes/login-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - unit/routes');
  QUnit.test('unit/routes/login-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'unit/routes/login-test.js should pass jshint.'); 
  });

});
define('my-new-app/tests/unit/routes/procedures/edit-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor('route:procedures/edit', 'Unit | Route | procedures/edit', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  ember_qunit.test('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });

});
define('my-new-app/tests/unit/routes/procedures/edit-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - unit/routes/procedures');
  QUnit.test('unit/routes/procedures/edit-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'unit/routes/procedures/edit-test.js should pass jshint.'); 
  });

});
define('my-new-app/tests/unit/routes/procedures/view-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor('route:procedures/view', 'Unit | Route | procedures/view', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  ember_qunit.test('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });

});
define('my-new-app/tests/unit/routes/procedures/view-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - unit/routes/procedures');
  QUnit.test('unit/routes/procedures/view-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'unit/routes/procedures/view-test.js should pass jshint.'); 
  });

});
define('my-new-app/tests/unit/routes/procedures-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor('route:procedures', 'Unit | Route | procedures', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  ember_qunit.test('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });

});
define('my-new-app/tests/unit/routes/procedures-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - unit/routes');
  QUnit.test('unit/routes/procedures-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'unit/routes/procedures-test.js should pass jshint.'); 
  });

});
define('my-new-app/tests/unit/routes/users-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleFor('route:users', 'Unit | Route | users', {
    // Specify the other units that are required for this test.
    // needs: ['controller:foo']
  });

  ember_qunit.test('it exists', function (assert) {
    var route = this.subject();
    assert.ok(route);
  });

});
define('my-new-app/tests/unit/routes/users-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - unit/routes');
  QUnit.test('unit/routes/users-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'unit/routes/users-test.js should pass jshint.'); 
  });

});
define('my-new-app/tests/unit/serializers/application-test', ['ember-qunit'], function (ember_qunit) {

  'use strict';

  ember_qunit.moduleForModel('application', 'Unit | Serializer | application', {
    // Specify the other units that are required for this test.
    needs: ['serializer:application']
  });

  // Replace this with your real tests.
  ember_qunit.test('it serializes records', function (assert) {
    var record = this.subject();

    var serializedRecord = record.serialize();

    assert.ok(serializedRecord);
  });

});
define('my-new-app/tests/unit/serializers/application-test.jshint', function () {

  'use strict';

  QUnit.module('JSHint - unit/serializers');
  QUnit.test('unit/serializers/application-test.js should pass jshint', function(assert) { 
    assert.ok(true, 'unit/serializers/application-test.js should pass jshint.'); 
  });

});
/* jshint ignore:start */

/* jshint ignore:end */

/* jshint ignore:start */

define('my-new-app/config/environment', ['ember'], function(Ember) {
  var prefix = 'my-new-app';
/* jshint ignore:start */

try {
  var metaName = prefix + '/config/environment';
  var rawConfig = Ember['default'].$('meta[name="' + metaName + '"]').attr('content');
  var config = JSON.parse(unescape(rawConfig));

  return { 'default': config };
}
catch(err) {
  throw new Error('Could not read config from meta tag with name "' + metaName + '".');
}

/* jshint ignore:end */

});

if (runningTests) {
  require("my-new-app/tests/test-helper");
} else {
  require("my-new-app/app")["default"].create({"name":"my-new-app","version":"0.0.0+01dc4f1d"});
}

/* jshint ignore:end */
//# sourceMappingURL=my-new-app.map