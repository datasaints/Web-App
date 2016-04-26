var app = angular.module('DataSaints', ['ui.router',
                                        'ngCookies'
                                        ]);

app.config(function($stateProvider, $urlRouterProvider) {
	$urlRouterProvider.otherwise('/login');
    
    $stateProvider
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
                },
                'widgets@home': {
        	        templateUrl: 'pages/top-widgets.html',
        	        controller: 'WidgetController'
                },
                'content@home': {
        	        templateUrl: 'pages/item-table.html',
        	        controller: 'TableController'
                }
            }
        })
        
        .state('update-item', {
            url: '/update-item',
            views: {
            	'': {
         	       templateUrl: 'pages/home.html',
            	},
                'navbar@update-item': {
                    templateUrl: 'pages/navbar.html',
                },
                'sidebar@update-item': {
                    templateUrl: 'pages/sidebar.html',
                },
                'widgets@update-item': {
        	        templateUrl: 'pages/top-widgets.html',
        	        controller: 'WidgetController'
                },
                'content@update-item': {
        	        templateUrl: 'pages/update-item.html',
        	        controller: 'ItemController'
                }
            }
        })
        
        .state('update-reader', {
            url: '/update-reader',
            views: {
            	'': {
         	       templateUrl: 'pages/home.html',
            	},
                'navbar@update-reader': {
                    templateUrl: 'pages/navbar.html',
                },
                'sidebar@update-reader': {
                    templateUrl: 'pages/sidebar.html',
                },
                'widgets@update-reader': {
        	        templateUrl: 'pages/top-widgets.html',
        	        controller: 'WidgetController'
                },
                'content@update-reader': {
        	        templateUrl: 'pages/edit-reader.html',
        	        controller: 'ReaderProfileController'
                }
            }
        })
        
        .state('advanced-search', {
            url: '/advanced-search',
            views: {
            	'': {
         	       templateUrl: 'pages/home.html',
            	},
                'navbar@advanced-search': {
                    templateUrl: 'pages/navbar.html',
                },
                'sidebar@advanced-search': {
                    templateUrl: 'pages/sidebar.html',
                },
                'widgets@advanced-search': {
        	        templateUrl: 'pages/top-widgets.html',
        	        controller: 'WidgetController'
                },
                'content@advanced-search': {
        	        templateUrl: 'pages/edit-reader.html',
        	        controller: 'ReaderProfileController'
                }
            }
        })
    
        // Login state
        .state('login', {
            url: '/login',
            controller: 'LoginController',
            templateUrl: 'pages/login.html'
        });
    
/*
    // use the HTML5 History API
    $locationProvider.html5Mode(true);*/
});

app.factory('itemService', function($http) {

    var getData = function() {

        // Angular $http() and then() both return promises themselves 
        return $http({method:"GET", url:"/datasaints/getItems"}).then(function(result){

            // What we return here is the data that will be accessible 
            // to us after the promise resolves
            return result.data;
        });
    };


    return { getData: getData };
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


