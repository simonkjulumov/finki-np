(function() {
  var Code = {

    // this is our top level module, the module that sets up the namespace and secondary level modules

    Modules: {},

    Utils: {},
    
    Editor: {},

    Events: $({}),
    
    Constants : {
    	MAX_TEST_CASES : 25
    },

    init: function () {

      // here we are looping round all of the modules in our app.Modules object. We could have an exclude array for modules
      // that we don't want to be immediately initialised. We could initialise them later on in our application lifecycle

      for( var x in Code.Modules ) {
    	  Code.Modules[x].init();
      }

      // the most useful part of this is Events. Modules shouldn't know about each other, so they're completely decoupled. We use
      // app.Events to 'trigger' and use 'on' to send/receive messages and data around our application. The 'trigger' function
      // takes data as it's second parameter which is accessible in the 'params' object in the receiving function, i.e. look at the
      // 'render' function within the 'jesse' module

      Code.Events.trigger( 'render' );
    }
  };

  // expose the Code globally
  window.Code = Code;
  
  // enable tooltips
  
/*  $('a[rel=tooltip]').tooltip({
		'placement': 'bottom'
  });*/
})();