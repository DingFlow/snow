/* 

设置全局对象

*/

define([],function(){
    /* 全局对象 */
    let ortum_field;//components列表栏
    // let ortum_components;//绘制表单的区域栏
    let ortum_item={};//各个可以拖拽的表单元素
    let ortum_nowDragObj;//现在正在拖动的表单元素
    let ortum_contextMenuObj;//右键点击菜单对象

    let ortum_edit_component;//正在编辑的对象
    let ortum_codemirrorJS_setVal;//设置codemirror的初始值
    let ortum_codemirrorJS_save;//codemirror关闭函数

    let ortum_preview_windowSon;//打开的预览窗口
    let ortum_preview_windowSonUrl;//打开的预览窗口Blob url

    let ortum_createDom_frame = "Bootstrap";//默认创建组件使用的框架

    let ortum_life_function;//全局生命周期json

    let ortum_life_json={};//参数json

    let ortum_life_Css;//全局css
    let ortum_codemirrorCSS_setVal;
    let ortum_codemirrorCSS_save;

    let ortum_codemirrorHTML_setVal;
    let ortum_codemirrorHTML_save;

    let ortum_replace_item=null;//被替换对象
    let ortum_active_item=null;//替换对象

    let ortum_keydown_event = null;//键盘对象

    let ortum_attributesArr_setVal;
    let ortum_attributesArr_save;

    let ortum_shadowDiv_actsMenu = null;//操作菜单栏

    let ortum_clickWillDo = {};//只要触发点击事件，就会执行

    let ortum_save_checkVersion = false;//检测版本号保存



    return{
        ortumField:ortum_field,
        // ortumComponents:ortum_components,
        ortumItem:ortum_item,
        ortumNowDragObj:ortum_nowDragObj,
        ortumCOntextMenuObj:ortum_contextMenuObj,
        ortum_edit_component:ortum_edit_component,

        ortum_codemirrorJS_setVal:ortum_codemirrorJS_setVal,
        ortum_codemirrorJS_save:ortum_codemirrorJS_save,

        ortum_createDom_frame:ortum_createDom_frame,

        ortum_preview_windowSon:ortum_preview_windowSon,
        ortum_preview_windowSonUrl:ortum_preview_windowSonUrl,

        ortum_life_function:ortum_life_function,
        ortum_life_json:ortum_life_json,
        ortum_life_Css:ortum_life_Css,
        ortum_codemirrorCSS_setVal:ortum_codemirrorCSS_setVal,
        ortum_codemirrorCSS_save:ortum_codemirrorCSS_save,

        ortum_replace_item:ortum_replace_item,
        ortum_active_item:ortum_active_item,
        ortum_keydown_event:ortum_keydown_event,

        ortum_attributesArr_setVal:ortum_attributesArr_setVal,
        ortum_attributesArr_save:ortum_attributesArr_save,

        ortum_shadowDiv_actsMenu:ortum_shadowDiv_actsMenu,

        ortum_clickWillDo:ortum_clickWillDo,


        ortum_codemirrorHTML_setVal:ortum_codemirrorHTML_setVal,
        ortum_codemirrorHTML_save:ortum_codemirrorHTML_save,

        ortum_save_checkVersion:ortum_save_checkVersion,
    }
})
