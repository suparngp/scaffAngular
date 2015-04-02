describe('Service: #MODULENAME#.#COMPONENTNAME#', function () {

    // load the service's module
    beforeEach(module('#MODULENAME#'));

    // instantiate service
    var service;

    //update the injection
    beforeEach(inject(function (#COMPONENTNAME#) {
        service = #COMPONENTNAME#;
    }));

    /**
     * @description
     * Sample test case to check if the service is injected properly
     * */
    it('should be injected and defined', function () {
        expect(service).toBeDefined();
    });
});