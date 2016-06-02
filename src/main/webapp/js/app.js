var app = angular.module('DataSaints', ['ui.router',
                                        'ngCookies',
                                        'ngRoute',
                                        'ngAnimate',
                                        'ui.bootstrap',
                                        'angularModalService',
                                        'oc.lazyLoad'
                                        ]);

app.config(function($stateProvider, $urlRouterProvider) { 
	$urlRouterProvider.otherwise('/login');
    
    $stateProvider
	    // Login state
	    .state('login', {
	        url: '/login',
	        controller: 'LoginController',
	        templateUrl: 'pages/login.html'
	    })    
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
		});
    
    /*$stickyStateProvider.enableDebug(true);*/

    
/*
    // use the HTML5 History API
    $locationProvider.html5Mode(true);*/
});

 
app.controller('ReaderProfileController', function($scope) {
 
    $scope.message = 'This is the reader profile controller';
 
});


