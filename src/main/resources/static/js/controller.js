var accountsApp = angular.module('accountsApp', [ 'ui.bootstrap' ]);

accountsApp.directive('fileModel', [ '$parse', function($parse) {
    return {
        restrict : 'A',
        link : function(scope, element, attrs) {
            var model = $parse(attrs.fileModel);
            var modelSetter = model.assign;

            element.bind('change', function() {
                scope.$apply(function() {
                    modelSetter(scope, element[0].files[0]);
                });
            });
        }
    };
} ]);

accountsApp.service('transactionService', [ '$http', function($http) {
    this.uploadToUrl = function(file, url) {
        var data = new FormData();
        data.append('file', file);

        return $http.post(url, data, {
            transformRequest : angular.identity,
            headers : {
                'Content-Type' : undefined
            }
        }).then(function(response) {
            return response.data;
        });
    };
    this.getTransactions = function() {
        return $http.get("transactions").then(function(response) {
            return response.data;
        });
    };
} ]);

accountsApp.service('categoryService', [ '$http', function($http) {
    this.getCategories = function() {
        return $http.get("categories").then(function(response) {
            return response.data;
        });
    };
    this.createCategory = function(name) {
        return $http.post("categories", {
            "name" : name
        }).then(function(response) {
            return response.data;
        });
    }
} ]);

accountsApp.controller('TransactionController', [ '$scope', 'transactionService', 'categoryService',
        function($scope, transactionService, categoryService) {
            $scope.uploadTransactions = function() {
                var file = $scope.transactionFile;
                transactionService.uploadToUrl(file, "transactionFile").then(function(data) {
                    $scope.setTransactions(data);
                });
            };
            $scope.startChanged = function() {
                $scope.startDate.setHours(0, 0, 0, 0);
                $scope.endDateOptions.minDate = $scope.startDate;
            };
            $scope.endChanged = function() {
                $scope.endDate.setHours(0, 0, 0, 0);
                $scope.startDateOptions.maxDate = $scope.endDate;
            };
            $scope.openStartDate = function() {
                $scope.startDatePopup.opened = true;
            };
            $scope.openEndDate = function() {
                $scope.endDatePopup.opened = true;
            };
            $scope.startDatePopup = {
                opened : false
            };
            $scope.endDatePopup = {
                opened : false
            };
            $scope.sortBy = function(parameterName) {
                $scope.reverse = ($scope.orderBy === parameterName) ? !$scope.reverse : false;
                $scope.orderBy = parameterName;
            }
            $scope.init = function() {
                $scope.reverse = false;
                $scope.orderBy = "date";
                transactionService.getTransactions().then(function(data) {
                    $scope.setTransactions(data);
                });
                categoryService.getCategories().then(function(data) {
                    $scope.categories = data;
                });
            };
            $scope.setTransactions = function(data) {
                $scope.allTransactions = data;
                $scope.transactions = data;

                if (data.length > 0) {
                    $scope.startDate = new Date(data[0].date).setHours(0, 0, 0, 0);
                    $scope.endDate = new Date(data[data.length - 1].date).setHours(0, 0, 0, 0);
                }
            };
            $scope.createCategory = function() {
                categoryService.createCategory($scope.newCategoryName).then(function(data) {
                    categoryService.getCategories().then(function(data) {
                        $scope.categories = data;
                    });
                });
            };
        } ]);

accountsApp.filter("transactionFilter", function() {
    return function(items, from, to, category) {
        var result = [];
        if (items) {
            for (var i = 0; i < items.length; i++) {
                var itemDate = new Date(items[i].date);
                itemDate.setHours(0, 0, 0, 0);
                if (itemDate >= from && itemDate <= to) {
                    if (category) {
                        if ($.inArray(items[i].description, category.matchers) > -1) {
                            result.push(items[i]);
                        }
                    } else {
                        result.push(items[i]);
                    }
                }
            }
        }
        return result;
    };
});
