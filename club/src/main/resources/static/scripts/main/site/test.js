(function (window, undefined) {
    var Popup = Base.getClass('main.component.Popup');
    var PopupAdd = Base.getClass('main.component.PopupAdd');

    Base.ready({
        initialize: fInitialize,
        initPopup: fInitPopup,
        initAdd: fInitAdd
    });

    function fInitialize() {
        var that = this;
        // that.initPopup();
        that.initAdd();
    }

    function fInitPopup() {
        var that = this;
        new Popup({
            title: '测试'
        });
    }

    function fInitAdd() {
        var that = this;
        PopupAdd.show({
            ok: function () {
                window.location.reload();
            }
        });
    }

})(window);