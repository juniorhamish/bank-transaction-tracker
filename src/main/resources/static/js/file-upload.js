var accountsApp = angular.module('accountsApp', []);

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

accountsApp.service('fileUpload', [ '$http', function($http) {
    this.uploadToUrl = function(file, url) {
        var data = new FormData();
        data.append('file', file);

        return $http.post(url, data, {
            transformRequest : angular.identity,
            headers : {
                'Content-Type' : undefined
            }
        }).then(function(response) {
            console.log(response);

            return response.data;
        });
    };
} ]);

accountsApp.controller('FileUploadController', [ '$scope', 'fileUpload', function($scope, fileUpload) {
    $scope.uploadTransactions = function() {
        var file = $scope.transactionFile;
        fileUpload.uploadToUrl(file, "transactionFile").then(function(data) {
            $scope.transactions = data;
        });
    };
} ]);
