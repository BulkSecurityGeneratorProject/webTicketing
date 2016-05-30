(function() {
    'use strict';

    angular
        .module('demoApp')
        .controller('ActionDialogController', ActionDialogController);

    ActionDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Action', 'ActionLang', 'Request', 'Subcategory'];

    function ActionDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Action, ActionLang, Request, Subcategory) {
        var vm = this;

        vm.action = entity;
        vm.clear = clear;
        vm.save = save;
        vm.actionlangs = ActionLang.query();
        vm.requests = Request.query();
        vm.subcategories = Subcategory.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.action.id !== null) {
                Action.update(vm.action, onSaveSuccess, onSaveError);
            } else {
                Action.save(vm.action, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('demoApp:actionUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
