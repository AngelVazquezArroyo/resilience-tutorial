var module = angular.module('resilienceTutorial', ['btford.markdown','ui.bootstrap']);

module.controller('ReadmeCtrl', function($scope, readme, $uibModalInstance) {
  $scope.readme = readme;
  $scope.dismiss = function() {
    $uibModalInstance.dismiss();
  };
});


module.controller('resilienceTutorialCtrl', function($scope, $http, $uibModal) {
    $scope.showReadme = function() {
      $http.get('./README.md')
        .then(function(response) {
            $uibModal.open({
              templateUrl: 'readme-modal.html', controller: 'ReadmeCtrl',size: 'lg',
                resolve: {
                  readme: function() {
                    return response.data;
                  }
                }
            });
        })
    .catch(function() {
      window.open('./README.md');
      });
    };

  $scope.sections = [
    {
      title: 'Case Study',
      directory: 'exercise_01_bulkheads',
      story: true,
      solutions: false,
      slides: false,
      exercise: false
    },
    {
      id: 1,
      title: 'Bulkheads',
      directory: 'exercise_01_bulkheads',
      exercise: true,
      solutions: true,
      slides: true
    }
  ];
});
