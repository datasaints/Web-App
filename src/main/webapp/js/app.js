var app = angular.module('DataSaints', ['ui.router',
                                        'ngCookies',
                                        'ngRoute',
                                        'ngAnimate',
                                        'ui.bootstrap',
                                        'angularModalService',
                                        'oc.lazyLoad'/*,
                                        'ct.ui.router.extras'*/
                                        ]);

app.config(function($stateProvider, $urlRouterProvider) { //$stickyStateProvider
	$urlRouterProvider.otherwise('/login');
    
    $stateProvider
   /* .state('home', {
    	    url: '/home',
    	    template: '<div>Home</div>  <a ui-sref="home.news"> change state</a><br><a ui-sref="home.main"> change state2</a><div ui-view></div>'
    	  })
    	  .state('home.main', {
    	    url: '/main',
    	    template: '<div>Main News</div>'
    	  })
    	  .state('home.news', {
    	    url: '/news',
    	    template: '<div>List of News</div><ul><li ng-repeat="new in news"><a>{{new.title}}</a></li></ul>',
    	    controller: function($scope){
    	      $scope.news = [{ title: 'First News' }, { title: 'Second News' }];
    	    }
    	  })
*/
        // Home states and nested views
        .state('home', {
            url: '/home',
            views: {
            	'': {
        	       templateUrl: 'pages/home.html',
            	},
                'navbar@home': {
                    templateUrl: 'pages/navbar.html',
                },
                'sidebar@home': {
                    templateUrl: 'pages/sidebar.html',
                    controller: 'SidebarController'
                },
                'widgets@home': {
        	        templateUrl: 'pages/top-widgets.html',
        	        controller: 'WidgetController'
                }
            },
            /*sticky: true,
            dsr: true*/
        })
        .state('home.table', {
			url: '/item-table/:location',
			templateUrl: 'pages/item-table.html',
			controller: 'TableController'
    	  })
		.state('home.update-item', {
			url: '/update-item',
			templateUrl: 'pages/update-item.html',
			controller: 'ItemController'
		})
		.state('home.advanced-search', {
			url: '/advanced-search',
			templateUrl: 'pages/adv-search.html',
			controller: 'SearchController'
		})
		.state('home.update-reader', {
			url: '/update-reader',
			templateUrl: 'pages/update-reader.html',
			controller: 'ReaderProfileController'
		})

        // Login state
        .state('login', {
            url: '/login',
            controller: 'LoginController',
            templateUrl: 'pages/login.html'
        });
    
    /*$stickyStateProvider.enableDebug(true);*/

    
/*
    // use the HTML5 History API
    $locationProvider.html5Mode(true);*/
});


app.factory('filterByFactory', function () {
    var filterBy = { };

    return {
        getFilter: function () {
            return filterBy;
        },
        setFilter: function(value) {
        	filterBy = value;
        }
    };
});
 
 
app.controller('ReaderProfileController', function($scope) {
 
    $scope.message = 'This is Show orders screen';
 
});


