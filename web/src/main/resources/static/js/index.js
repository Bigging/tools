function black_phone_list_update() {
    var temporaryId = null
    const id = $("#black_phone_list_input").val();
    if (id){
        temporaryId = id
    }
    $.ajax({
        type : 'post',
        url: "/tools/api/refresh/refreshBlackPhoneList",
        data:  {temporaryId},
        success: function (res) {
            alert("开始执行，请检查执行日志")
        },
        error: function (err) {
            alert('更新数据失败')
        }
    })
}