define([
    "../../codemirror/lib/codemirror",

    "CSS!../../codemirror/lib/codemirror.css",
    "../../codemirror/mode/javascript/javascript",
    "../../codemirror/mode/css/css",
    "../../codemirror/mode/xml/xml",

    "CSS!../../codemirror/theme/monokai.css",
    "CSS!../../codemirror/addon/fold/foldgutter.css",
    "../../codemirror/addon/fold/brace-fold",
    "../../codemirror/addon/fold/foldcode",
    "../../codemirror/addon/fold/foldgutter",
    "../../codemirror/addon/fold/comment-fold",

    "../../codemirror/keymap/sublime",//快捷键


    "CSS!../../codemirror/addon/hint/show-hint.css",//提示
    "../../codemirror/addon/hint/show-hint",
    "../../codemirror/addon/hint/javascript-hint",

    "CSS!../../codemirror/addon/display/fullscreen.css",//全屏
    "../../codemirror/addon/display/fullscreen",

    "CSS!../../codemirror/addon/lint/lint.css",//语法检查
    "../../codemirror/addon/lint/lint",
    "../../codemirror/addon/lint/javascript-lint",
    
    "../../codemirror/addon/display/placeholder",//placeholder

    "../../codemirror/addon/selection/active-line",//单行高亮
],

function(CodeMirror){
    //初始化编辑js
    let initJs = function(){
        const option = {
            // value: "function myScript(){return 100;}",
            mode:  "javascript",
            theme:"monokai",
            lineNumbers:true,//行号
            lineWrapping: true,
            autofocus:true,
            keyMap:"sublime",
            foldGutter: true,//开启折叠
            gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter","CodeMirror-lint-markers"],

            //提示
            hintOptions:{
                // hint:CodeMirror.hint.javascript,
                completeSingle:false,
            },
            //语法检查
            // gutters: ["CodeMirror-lint-markers"],
            lint: true,

            placeholder:"光标在编辑器上时，F11全屏，ESC退出",

            styleActiveLine:true,//光标行高亮
        }
    
        let myCodeMirror = CodeMirror.fromTextArea(document.getElementById('ortum_codeMirror'), option);
        // myCodeMirror.setSize("100%","300px");
        // myCodeMirror.setValue("function myScript(){return 100;}");

        myCodeMirror.refresh();

        //显示提示  
        myCodeMirror.on("inputRead", () => {
            myCodeMirror.showHint();
        }); 
    
        //设置全屏快捷键
        myCodeMirror.setOption("extraKeys", {
            F11:function(cm) {
                cm.setOption("fullScreen", true)
            },
            Esc:function(cm) {
                cm.setOption("fullScreen", false)
                myCodeMirror.refresh()
            },
        });

        
        return myCodeMirror
    }

    //初始化编辑CSS
    let initCss = function(){
        const option = {
            // value: "function myScript(){return 100;}",
            mode:  "css",
            theme:"monokai",
            lineNumbers:true,//行号
            lineWrapping: true,
            autofocus:true,
            keyMap:"sublime",
            foldGutter: true,//开启折叠
            gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter","CodeMirror-lint-markers"],

            //提示
            hintOptions:{
                // hint:CodeMirror.hint.javascript,
                completeSingle:false,
            },
            //语法检查
            // gutters: ["CodeMirror-lint-markers"],
            lint: true,

            placeholder:"光标在编辑器上时，F11全屏，ESC退出",

            styleActiveLine:true,//光标行高亮
        }

        let myCodeMirror = CodeMirror.fromTextArea(document.getElementById('ortum_codeMirror'), option);

        myCodeMirror.refresh();
        //显示提示
        myCodeMirror.on("inputRead", () => {
            myCodeMirror.showHint();
        });

        //设置全屏快捷键
        myCodeMirror.setOption("extraKeys", {
            F11:function(cm) {
                cm.setOption("fullScreen", true)
            },
            Esc:function(cm) {
                cm.setOption("fullScreen", false)
                myCodeMirror.refresh()
            },
        });
        return myCodeMirror
    }

    //初始化编辑html
    let initHtml = function(){
        const option = {
            // value: "function myScript(){return 100;}",
            mode:  "text/html",
            theme:"monokai",
            lineNumbers:true,//行号
            lineWrapping: true,
            autofocus:true,
            keyMap:"sublime",
            foldGutter: true,//开启折叠
            gutters: ["CodeMirror-linenumbers", "CodeMirror-foldgutter","CodeMirror-lint-markers"],

            //提示
            hintOptions:{
                // hint:CodeMirror.hint.javascript,
                completeSingle:false,
            },
            //语法检查
            // gutters: ["CodeMirror-lint-markers"],
            lint: true,

            placeholder:"光标在编辑器上时，F11全屏，ESC退出",

            styleActiveLine:true,//光标行高亮
        }

        let myCodeMirror = CodeMirror.fromTextArea(document.getElementById('ortum_codeMirror'), option);

        myCodeMirror.refresh();
        //显示提示
        myCodeMirror.on("inputRead", () => {
            myCodeMirror.showHint();
        });

        //设置全屏快捷键
        myCodeMirror.setOption("extraKeys", {
            F11:function(cm) {
                cm.setOption("fullScreen", true)
            },
            Esc:function(cm) {
                cm.setOption("fullScreen", false)
                myCodeMirror.refresh()
            },
        });
        return myCodeMirror
    }
    

    return {
        initJs:initJs,
        initCss:initCss,
        initHtml:initHtml,
    }
})