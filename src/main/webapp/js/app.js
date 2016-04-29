var app = angular.module('DataSaints', ['ui.router',
                                        'ngCookies'
                                        ]);

app.config(function($stateProvider, $urlRouterProvider) {
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
                },
                'widgets@home': {
        	        templateUrl: 'pages/top-widgets.html',
        	        controller: 'WidgetController'
                }
            }
        })
        .state('home.table', {
			url: '/item-table',
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
/*
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
    */
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


