function switchTableAct(act="edit",settings={}){
    switch (act) {
        case "new":
            $("#ortum_table_name").val('');
            $("#ortum_table_code").val('');
            $("#ortum_table_info .ortum_table_method").eq(0).text("新增")
                .attr("data-method","newPCTable")
                .removeAttr("data-formid")
                .removeAttr("data-version")
                .removeClass("ortum_editPC_color")
                .addClass("ortum_newPC_color");
            $("#ortum_table_act .ortum_tableAct_icon").removeClass("ortum_editPC_color").addClass("ortum_newPC_color");
            $("body").removeClass("body_bgc_editPC").addClass("body_bgc_newPC");
            break;
        case "edit":
            settings.formName && $("#ortum_table_name").val(settings.formName);
            settings.formCode && $("#ortum_table_code").val(settings.formCode);
            $("#ortum_table_info .ortum_table_method").eq(0).text("修改")
                .attr("data-method","editPCTable")
                .attr("data-formid",settings.formId)
                .attr("data-version",settings.version || 0)
                .removeClass("ortum_newPC_color").addClass("ortum_editPC_color");
            $("#ortum_table_act .ortum_tableAct_icon").removeClass("ortum_newPC_color").addClass("ortum_editPC_color");
            $("body").removeClass("body_bgc_newPC").addClass("body_bgc_editPC");
            break;
        default:
            break;
    }
};



function showOrtumLoading(show){
    if(show === false){
        $(".ortum_loading").eq(0).css("display","none");
    }else if(show === true){
        $(".ortum_loading").eq(0).css("display","flex");
    }else{
        if($(".ortum_loading").eq(0).css("display") == "none"){
            $(".ortum_loading").eq(0).css("display","flex");
        }else{
            $(".ortum_loading").eq(0).css("display","none");
        }
    }
}

// 绘制区组件替换关闭事件监听
$('#ortum_replaceItem_model').on('hidden.bs.modal', function (e) {
    require(['feature','global'],function(Feature,Global){
        Global.ortum_replace_item = null;
        Global.ortum_active_item = null;
    });
});
// 组件替换
$("#ortum_repalceItemBtn").on("click",function(){
    require(['feature','global',"assist"],function(Feature,Global,Assist){
        let replaceItemParentDom =  $(Global.ortum_replace_item).parent();
        let activeItemParentDom =  $(Global.ortum_active_item).parent();
        //TODO 旧表格删除后，此处if可以删除
        if(activeItemParentDom.parents(".ortum_item").eq(0).hasClass("ortum_bootstrap_table") || replaceItemParentDom.parents(".ortum_item").eq(0).hasClass("ortum_bootstrap_table")){
            Assist.dangerTip("旧表格不支持组件替换，请将旧表单换成新表格",2000);
        }else{
            $(Global.ortum_replace_item).eq(0).replaceWith(Global.ortum_active_item);

            if($(activeItemParentDom).hasClass("ortum_bootstrap_td") && $(activeItemParentDom).parents(".ortum_item").eq(0).hasClass('ortum_bootstrap_newTable')){
                require("BootstrapNewTable").sonOrtumItemDelete(activeItemParentDom);
            };
        }
        Global.ortum_replace_item = null;
        Global.ortum_active_item = null;
        $('#ortum_replaceItem_model').modal('hide');
    });
});
// 组件交换
$("#ortum_exchangeItemBtn").on("click",function(){
    require(['feature','global',"assist"],function(Feature,Global,Assist){
        let replaceItemParentDom =  $(Global.ortum_replace_item).parent();
        let activeItemParentDom =  $(Global.ortum_active_item).parent();
        //TODO 旧表格删除后，此处if可以删除
        if(replaceItemParentDom.parents(".ortum_item").eq(0).hasClass("ortum_bootstrap_table") || activeItemParentDom.parents(".ortum_item").eq(0).hasClass("ortum_bootstrap_table")){
            Assist.dangerTip("旧表格不支持组件交换，请将旧表单换成新表格",2000);
        }else{
            $(Global.ortum_replace_item).eq(0).before("<div id='ortum_exchangeA_tempObj' class='ortum_display_NONE'></div>");
            $(Global.ortum_active_item).eq(0).before("<div id='ortum_exchangeB_tempObj' class='ortum_display_NONE'></div>");
            $("#ortum_exchangeA_tempObj").eq(0).replaceWith(Global.ortum_active_item);
            $("#ortum_exchangeB_tempObj").eq(0).replaceWith(Global.ortum_replace_item);
        }
        Global.ortum_replace_item = null;
        Global.ortum_active_item = null;

        $('#ortum_replaceItem_model').modal('hide');
    });
});

//获取location信息，决定编辑状态
$(function(){
    let search = window.location.search;
    search = search.replace(/^(\?)/,'');
    search = search.replace(/(\&)/g,',');
    let searchArr = [];
    let searchObj = {};

    searchArr = search.split(",")
    searchArr.forEach((item)=>{
        if(item){
            let name = item.match(/^\s*(\S)*?\s*(?=[=])/)[0];
            let value = item.match(/(?<=[=])\s*(\S)*?\s*$/)[0];
            searchObj[name] = value;
        }
    })
    //修改表单
    if(searchObj.method && searchObj.method=="editPCTable" && searchObj.formId){
        switchTableAct("edit",{formId:searchObj.formId,version:0})
    };

    //打开更多设置
    $(".ortum_form_moreSetting").off("click.setting").on('click.setting',function (e) {
        require(['feature','global'],function(Feature,Global){
            $('#ortum_middle_dialog').modal({
                "backdrop":"static",
            })
            $("#ortum_middle_model_content").load("/from/interface/moreSettings",function(){

                $('#ortum_middle_model_wait').hide();
            });
        });
    });

    //设置滚动条
    $(".ortum_mCustomScrollbar").mCustomScrollbar({
        theme:"dark-3",
        scrollInertia:0,//将滚动动量的量设置为动画持续时间（以毫秒为单位）
        scrollButtons:{enable:true,},//滚动按钮，滚动量
        mouseWheel:{deltaFactor:10},//鼠标滚动量
    });

    //只要触发点击事件，就会执行Global.ortum_clickWillDo中的函数
    $(document).on("click.all","*",function(e){
        require(['feature','global'],function(Feature,Global){
            for(let fun in Global.ortum_clickWillDo){
                if(Global.ortum_clickWillDo.hasOwnProperty(fun)){
                    Global.ortum_clickWillDo[fun](e);
                }
            }
        })
    });
});



let showTipSetTime;//定时器

//初始化提示
$(function () {
    $('[data-toggle="tooltip"]').tooltip()
});
//菜单栏事件绑定
$('#ortum_table_act').on('click','.iconfont',function(e){
    //导入
    if($(this).hasClass('icon-daoru')){
        $('#ortum_import_file').click();
        return;
    }
    //导出
    if($(this).hasClass('icon-daochu')){
        require(['feature'],function(Feature){
            // Feature.exportFileListen(e);
            Feature.exportJsonFileListen(e);
        })
        return;
    }
    //新建
    if($(this).hasClass('icon-xinjian')){
        window.open("/from/interface")
        return;
    }
    //清空
    if($(this).hasClass('icon-qingkong')){
        require(['feature','global'],function(Feature,Global){
            if(!confirm("确定清空吗？")){
                return;
            };

            Global.ortum_edit_component=undefined;//正在编辑的对象
            Global.ortum_codemirrorJS_setVal=undefined;;//设置codemirror的初始值
            Global.ortum_codemirrorJS_save=undefined;;//codemirror关闭函数
        
            Global.ortum_preview_windowSon=undefined;;//打开的预览窗口
            Global.ortum_preview_windowSonUrl=undefined;;//打开的预览窗口Blob url
        
            Global.ortum_life_function=undefined;;//全局生命周期json
            Global.ortum_life_json={};//参数json

            $('#ortum_collapseOne .form-group').show();
            $('#ortum_collapseOne input').each(function(){
                if($(this).attr("type")!="checkbox" && $(this).attr("type")!="radio"){
                    $(this).val('');
                }
            });
            $('#ortum_collapseOne textarea').val('');
            $('#ortum_collapseOne input[type=radio]').removeProp("checked");
            $('#ortum_collapseOne input[type=checkbox]').removeProp("checked");
            $('#ortum_collapseOne select').val('');
            $("#ortum_field").empty();
            $("#originState").removeClass("ortum_display_NONE");
        })
        return;
    }

    //保存
    if($(this).hasClass('icon-baocun')){
        require(['feature','assist','settings','global'],function(Feature,Assist,Settings,Global){
            let tableName = $("#ortum_table_name").val().trim();
            let tableCode = $("#ortum_table_code").val().trim();
            let actWay = $(".ortum_table_method").eq(0).attr('data-method') || "newPCTable";
            let formId = $("#ortum_table_info .ortum_table_method").eq(0).attr("data-formid") || '';
            let formVersion = $("#ortum_table_info .ortum_table_method").eq(0).attr("data-version") || 0;

            //保存时是否 不检测版本
            let SaveNoCheckVersion = Assist.ortumLocalStorage.getItem("SaveNoCheckVersion");

            if(!tableName){
                Assist.dangerTip("表单名称不可为空")
                return;
            }
            if(!tableCode){
                Assist.dangerTip("表单编号不可为空")
                return;
            }
            let ortumJson = Feature.getFormContentJson("id",{id:"ortum_field",HasProperties:true});
            let ortumJS = Global.ortum_life_function;
            let ortumSet = Global.ortum_life_json;
            let ortumCss = Global.ortum_life_Css;
            console.log("-------ortumJson---------------"+JSON.stringify(ortumJson));
            //babel转义
            if(Assist.ortumLocalStorage.getItem("BabelTransferCode")){
                ortumJS = Assist.getDetailType(ortumJS)==="Object" ? JSON.parse(JSON.stringify(ortumJS)) : undefined;
                Feature.transformOrtumJsonBrowerCompatible(ortumJson);
                //转义生命周期三个函数
                if(ortumJS && ortumJS.beigin_function){
                    ortumJS.beigin_function = {
                        source:ortumJS.beigin_function,
                        babel: Feature.transformJsBrowerCompatible(ortumJS.beigin_function),
                    }
                }
                if(ortumJS && ortumJS.completed_function){
                    ortumJS.completed_function = {
                        source:ortumJS.completed_function,
                        babel: Feature.transformJsBrowerCompatible(ortumJS.completed_function),
                    }
                }
                if(ortumJS && ortumJS.submit_function){
                    ortumJS.submit_function = {
                        source:ortumJS.submit_function,
                        babel: Feature.transformJsBrowerCompatible(ortumJS.submit_function),
                    }
                }
            }
            // console.log(ortumJS)
            // debugger

            let getTitleAndName =  Feature.getTitleAndNameFun(ortumJson)//后端需要的数据
            let titleArr = getTitleAndName.titleArr;
            let nameArr = getTitleAndName.nameArr;


            showOrtumLoading(true);

            //获取localstore中的信息
            let CATARC_INFO_SYS = window.localStorage.getItem("CATARC_INFO_SYS");
            let ajaxJsom = {
                columnID:nameArr.toString(),
                columnName:titleArr.toString(),
                contentHtml:JSON.stringify({
                    ortumJson:ortumJson,
                    ortumJS:ortumJS,
                    ortumSet:ortumSet,
                    ortumCss:ortumCss,
                }),
                ortumJson:ortumJson,
                editor:"ortum",
               // editTime:new Date(),
                formCode:tableCode,
                formName:tableName,
                id:formId,
                version:formVersion*1 + 1,
            }
            if(actWay == "newPCTable"){
                ajaxJsom.dataSourceId = '';
                ajaxJsom.delFlag = '0';
                ajaxJsom.formWrite = '0';
            }

            //有formID时 校验版本号
            formId  && axios.get("/from/instance/saveFromInfo/"+formId)
                .then(function (res) {
                    if(res.data.code==0){
                        if(!res.data.data){
                            ajaxJsom.version = 1;
                            return axios.post("/catarc_infoSys/api/form?_ts="+(new Date()).getTime(),ajaxJsom)
                        }else if((res.data.data.version)*1 > formVersion*1 && !SaveNoCheckVersion){
                            Assist.dangerTip("当前版本号小于数据库的版本号！")
                            return Promise.reject({"noShowTip":true})
                        }else{
                            return axios.put("/catarc_infoSys/api/form?_ts="+(new Date()).getTime(),ajaxJsom)
                        }
                    }else{
                        Assist.dangerTip(res.data.message)
                    }
                })
                .then(function(res){
                    if(res.data.ok){
                        Assist.infoTip("保存成功");
                        //更新版本号和formId
                        switchTableAct("edit",{formName:res.data.data.formName,formCode:res.data.data.formCode,formId:res.data.data.id,version:res.data.data.version})
                    }else{
                        Assist.dangerTip(res.data.message)
                    }
                    showOrtumLoading(true);
                })
                .catch(function (error) {
                    if(!error || !error.noShowTip){
                        Assist.dangerTip("保存失败");
                    }
                    console.error(error);
                }).finally(function () {
                    showOrtumLoading(false);
                });
            //无formID时 不检查版本号
            console.log("-------ajaxJsom---------------"+JSON.stringify(ajaxJsom));
            !formId && axios.post("/from/instance/saveFromInfo?_ts="+(new Date()).getTime(),ajaxJsom)
                .then(function(res){
                    //alert(JSON.stringify(res))
                    if(res.data.code==0){
                        Assist.infoTip("保存成功");
                        closeItem();
                        //切换为编辑
                       // switchTableAct("edit",{formName:res.data.data.formName,formCode:res.data.data.formCode,formId:res.data.data.id,version:res.data.data.version})
                    }else{
                        Assist.dangerTip(res.data.msg);
                    }
                })
                .catch(function (error) {
                    console.error(error);
                    Assist.dangerTip("保存失败");
                }).finally(function () {
                    showOrtumLoading(false);
                });
        });
        return;
    }
    //编辑js
    if($(this).hasClass('icon-js')){
        require(["global","assist"],function(Global,Assist){
            $('#ortum_top_dialog_xl').modal({
                "backdrop":"static",
                // "focus":false,
                "keyboard":false,
            });
            let funStr = "";

            if(Global.ortum_life_function){
                //函数字符串
                funStr = "var ortum_life_function={beigin_function:"+ Global.ortum_life_function.beigin_function.toString() +",completed_function:"+ Global.ortum_life_function.completed_function.toString() +",submit_function:"+ Global.ortum_life_function.submit_function.toString() +"};";
            }else{
                funStr = "var ortum_life_function={beigin_function:function(){},completed_function:function(){},submit_function:function(){},};";
            }

            if(Global.ortum_life_json){
                funStr += "/*表单参数配置*/\nvar ortum_life_json=" + JSON.stringify(Global.ortum_life_json) +";";
            }
            //格式化
            funStr = js_beautify(funStr,2);
            
            Global.ortum_codemirrorJS_setVal = function(codeObj){
                codeObj.setValue(`/*函数名请勿编辑，只需编辑函数体内容\nbeigin_function函数是dom渲染前会执行的函数\ncompleted_function函数是dom渲染后会执行的函数*/\n${funStr}
                `)
            };
            Global.ortum_codemirrorJS_save = function(val){
                let packer = new Packer;
                let valFormat = packer.pack(val, 0, 0); 
                try{
                    eval(valFormat);
                    Global.ortum_life_function = {};
                    Global.ortum_life_function.beigin_function=ortum_life_function.beigin_function.toString().replace(/\n/g,'').replace(/(\s)+/g," ");
                    Global.ortum_life_function.completed_function=ortum_life_function.completed_function.toString().replace(/\n/g,'').replace(/(\s)+/g," ");
                    Global.ortum_life_function.submit_function=ortum_life_function.submit_function.toString().replace(/\n/g,'').replace(/(\s)+/g," ");
                    Global.ortum_life_json = ortum_life_json;
                }catch(err){
                    console.error(err);
                    Assist.dangerTip("编辑失败，请查看控制台！")
                }
            };
            $("#ortum_top_model_xl_content").load("/from/interface/codemirror",function(){
                $('#ortum_top_model_xl_wait').hide();
            });
        })
        return;
    }


    //编辑css
    if($(this).hasClass('icon-css1')){
        require(["global",'assist'],function(Global,Assist){
            $('#ortum_top_dialog_xl').modal({
                "backdrop":"static",
                // "focus":false,
                "keyboard":false,
            });
            let cssStr = "";

            if(Global.ortum_life_Css){
                cssStr = Assist.actCssCoder.format(Global.ortum_life_Css);
            }

            Global.ortum_codemirrorCSS_setVal = function(codeObj){
                codeObj.setValue(`/*****编辑CSS*****/\n` + cssStr);
            };
            Global.ortum_codemirrorCSS_save = function(val){
                let packCss = Assist.actCssCoder.pack(val);
                Global.ortum_life_Css = packCss;
            };
            $("#ortum_top_model_xl_content").load("/from/interface/codeMirrorCss",function(){
                $('#ortum_top_model_xl_wait').hide();
            });
        })
        return;
    }
    //预览
    if($(this).hasClass('icon-yulan')){
        require(['feature'],function(Feature){
            //html方式
            Feature.previewTableHtmlContent("ortum_field")
            //blob方式
            // Feature.previewTableBlobContent("ortum_field")
        })
        return;
    }

    $('#ortum_tip_content_danger').text("火速赶制中！！！").show()
    $('.ortum_tip').show();
    clearInterval(showTipSetTime)
    showTipSetTime = setTimeout(function(){
        $('.ortum_tip').hide();
        $('#ortum_tip_content_danger').hide()
    },1000)
})



//model弹窗 事件监听
$('#ortum_top_dialog').on('hidden.bs.modal', function (e) {
    $('#ortum_top_model_wait').show();
    $("#ortum_top_model_content").empty();
})

//model弹窗 事件监听
$('#ortum_top_dialog_xl').on('hidden.bs.modal', function (e) {
    $('#ortum_top_model_xl_wait').show();
    $("#ortum_top_model_xl_content").empty();
})


//关闭或刷新浏览器之前的操作，可以启用一个线程保存到服务器中
window.onbeforeunload = function (e) {
    //发送给后端进行保存数据
    // navigator.sendBeacon('http://localhost:3000/Beacon', 'foo=bar');

    e = e || window.event;
    // 兼容IE8和Firefox 4之前的版本
    if (e) {
        e.returnValue = '请确定所做修改已经保存！';
    }
    // Chrome, Safari, Firefox 4+, Opera 12+ , IE 9+
    return '请确定所做修改已经保存！';
};

var closeItem = function(dataId){
    var topWindow = $(window.parent.document);
    if($.common.isNotEmpty(dataId)){
        window.parent.$.modal.closeLoading();
        // 根据dataId关闭指定选项卡
        $('.menuTab[data-id="' + dataId + '"]', topWindow).remove();
        // 移除相应tab对应的内容区
        $('.mainContent .RuoYi_iframe[data-id="' + dataId + '"]', topWindow).remove();
        return;
    }
    var panelUrl = window.frameElement.getAttribute('data-panel');
    $('.page-tabs-content .active i', topWindow).click();
    if($.common.isNotEmpty(panelUrl)){
        $('.menuTab[data-id="' + panelUrl + '"]', topWindow).addClass('active').siblings('.menuTab').removeClass('active');
        $('.mainContent .RuoYi_iframe', topWindow).each(function() {
            if ($(this).data('id') == panelUrl) {
                $(this).show().siblings('.RuoYi_iframe').hide();
                return false;
            }
        });
    }
}


