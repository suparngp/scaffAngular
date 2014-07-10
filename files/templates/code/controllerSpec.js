describe('Controller: #COMPONENTNAME#', function () {

    // load the controller's module
    beforeEach(module('#MODULENAME#'));

    var ctrl,
        scope;

    // Initialize the controller and a mock scope
    beforeEach(inject(function ($controller, $rootScope) {
        scope = $rootScope.$new();
        ctrl = $controller('#COMPONENTNAME#', {
            $scope: scope
        });
    }));

    it('should be defined', function () {
        expect(ctrl).toBeDefined();
    });
});