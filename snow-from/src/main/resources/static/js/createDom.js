/* 创建不同框架下的不同组件 */

define(["BootstrapGrid","BootstrapInput","BootstrapRangeInput","BootstrapRadio","BootstrapTextarea",
"BootstrapCheckbox",
"BootstrapFile",
"BootstrapSwitch",
"BootstrapSelect",
"BootstrapLabel",
"BootstrapDate",
"BootstrapTable", "BootstrapButton","BootstrapButtonGroup",
"BootstrapIconButton","BootstrapH","BootstrapMultiGrid","BootstrapNewTable",
"BootstrapCustomHtml","CustomXmSelect",
],
function(BootstrapGrid,BootstrapInput,BootstrapRangeInput,BootstrapRadio,BootstrapTextarea,BootstrapCheckbox,
    BootstrapFile,BootstrapSwitch,BootstrapSelect,BootstrapLabel,BootstrapDate,BootstrapTable,BootstrapButton,BootstrapButtonGroup,
    BootstrapIconButton,BootstrapH,BootstrapMultiGrid,BootstrapNewTable,BootstrapCustomHtml,CustomXmSelect){
    /**
     * 创建栅格系统
     * @param {*} type 
     */
    let createGridDom = function(parentDom,type,moreProps=null){
        switch(type){
            case 'Bootstrap':
                return BootstrapGrid.GridDom(parentDom,moreProps)
                break;
            default:

        }
    }
    /**
     * 创建单行
     * @param {*} type 
     */
    let createInputDom = function(parentDom,type,moreProps=null){
        switch(type){
            case 'Bootstrap':
                return BootstrapInput.InputDom(parentDom,moreProps)
                break;
            default:

        }
    }
    /**
     * 创建多行
     * @param {*} type 
     */
    let createTextareaDom = function(parentDom,type,moreProps=null){
        switch(type){
            case 'Bootstrap':
                return BootstrapTextarea.TextareaDom(parentDom,moreProps)
                break;
            default:

        }
    }

    /**
     * 创建进度选择器
     * @param {*} type 
     */
    let createRangeInputDom = function(parentDom,type,moreProps=null){
        switch(type){
            case 'Bootstrap':
                return BootstrapRangeInput.RangeInputDom(parentDom,moreProps)
                break;
            default:

        }
    }
     /**
     * 创建单选框
     * @param {*} type 
     */
    let createRadioDom = function(parentDom,type,moreProps=null){
        switch(type){
            case 'Bootstrap':
                return BootstrapRadio.RadioDom(parentDom,moreProps)
                break;
            default:

        }
    }
    /**
     * 创建多选框
     * @param {*} type 
     */
    let createCheckboxDom = function(parentDom,type,moreProps=null){
        switch(type){
            case 'Bootstrap':
                return BootstrapCheckbox.CheckboxDom(parentDom,moreProps)
                break;
            default:

        }
    }
    /**
     * 创建文件上传
     * @param {*} type 
     */
    let createFileDom = function(parentDom,type,moreProps=null){
        switch(type){
            case 'Bootstrap':
                return BootstrapFile.FileDom(parentDom,moreProps)
                break;
            default:

        }
    }
    /**
     * 创建开关
     * @param {*} type 
     */
    let createSwitchDom = function(parentDom,type,moreProps=null){
        switch(type){
            case 'Bootstrap':
                return BootstrapSwitch.SwitchDom(parentDom,moreProps)
                break;
            default:

        }
    }
    /**
     * 创建select
     * @param {*} type 
     */
    let createSelectDom = function(parentDom,type,moreProps=null){
        switch(type){
            case 'Bootstrap':
                return BootstrapSelect.SelectDom(parentDom,moreProps)
                break;
            default:

        }
    }
    
    /**
     * 创建label
     * @param {*} type
     */
    let createLabelDom = function(parentDom,type,moreProps=null){
        switch(type){
            case 'Bootstrap':
                return BootstrapLabel.LabelDom(parentDom,moreProps)
                break;
            default:

        }
    }
    /**
     * 创建label
     * @param {*} type
     */
    let createDateDom = function(parentDom,type,moreProps=null){
        switch(type){
            case 'Bootstrap':
                return BootstrapDate.DateDom(parentDom,moreProps)
                break;
            default:

        }
    }

    /**
     * 创建table，不可以插入布局组件
     * @param {*} type
     */
    let createTableDom = function(parentDom,type,moreProps=null){
        switch(type){
            case 'Bootstrap':
                return BootstrapTable.TableDom(parentDom,moreProps)
                break;
            default:

        }
    }

    /**
     * 创建按钮
     * @param {*} type
     */
    let createButtonDom = function(parentDom,type,moreProps=null){
        switch(type){
            case 'Bootstrap':
                return BootstrapButton.ButtonDom(parentDom,moreProps)
                break;
            default:

        }
    }
    /**
     * 创建图标按钮
     * @param {*} type
     */
    let createIconButtonDom = function(parentDom,type,moreProps=null){
        switch(type){
            case 'Bootstrap':
                return BootstrapIconButton.IconButtonDom(parentDom,moreProps)
                break;
            default:
        }
    }

    /**
     * 创建按钮组
     * @param {*} type
     */
    let createButtonGroupDom = function(parentDom,type,moreProps=null){
        switch(type){
            case 'Bootstrap':
                return BootstrapButtonGroup.ButtonGroupDom(parentDom,moreProps)
                break;
            default:
        }
    }

    /**
     * 创建标题标签
     * @param {*} type
     */
    let createHDom = function(parentDom,type,moreProps=null){
        switch(type){
            case 'Bootstrap':
                return BootstrapH.HDom(parentDom,moreProps)
                break;
            default:
        }
    }


    /**
     * 创建多行柵格
     * @param {*} type
     */
    let createMultiGridDom = function(parentDom,type,moreProps=null){
        switch(type){
            case 'Bootstrap':
                return BootstrapMultiGrid.MultiGridDom(parentDom,moreProps)
                break;
            default:
        }
    }
    /**
     * 创建新版table，可以插入布局组件
     * @param {*} type
     */
    let createNewTableDom = function(parentDom,type,moreProps=null){
        switch(type){
            case 'Bootstrap':
                return BootstrapNewTable.NewTableDom(parentDom,moreProps)
                break;
            default:
        }
    }

    /**
     * 创建新版table，可以插入布局组件
     * @param {*} type
     */
    let createCustomHtmlDom = function(parentDom,type,moreProps=null){
        switch(type){
            case 'Bootstrap':
                return BootstrapCustomHtml.CustomHtmlDom(parentDom,moreProps)
                break;
            default:
        }
    }

    /**
     * 创建xmSelect
     * @param {*} type
     */
    let createXmSelectDom = function(parentDom,type,moreProps=null){
        switch(type){
            case 'Custom':
                return CustomXmSelect.XmSelectDom(parentDom,moreProps)
                break;
            default:
        }
    }

    //设置对应Bootstrap框架 生成组件的函数是否开启
    createGridDom.ortum_Bootstrap = true;
    createInputDom.ortum_Bootstrap = true;
    createTextareaDom.ortum_Bootstrap = true;
    createRangeInputDom.ortum_Bootstrap = true;
    createRadioDom.ortum_Bootstrap = true;
    createCheckboxDom.ortum_Bootstrap = true;
    createFileDom.ortum_Bootstrap = true;
    createSwitchDom.ortum_Bootstrap = true;
    createSelectDom.ortum_Bootstrap = true;
    createLabelDom.ortum_Bootstrap = true;
    createDateDom.ortum_Bootstrap = true;
    createTableDom.ortum_Bootstrap = true;
    createButtonDom.ortum_Bootstrap = true;
    createButtonGroupDom.ortum_Bootstrap = true;
    createIconButtonDom.ortum_Bootstrap = true;
    createHDom.ortum_Bootstrap = true;
    createMultiGridDom.ortum_Bootstrap = true;
    createNewTableDom.ortum_Bootstrap = true;
    createCustomHtmlDom.ortum_Bootstrap = true;

    createXmSelectDom.ortum_Custom = false;
    return {
        createGridDom,
        createInputDom,
        createTextareaDom,
        createRangeInputDom,
        createRadioDom,
        createCheckboxDom,
        createFileDom,
        createSwitchDom,
        createSelectDom,
        createLabelDom,
        createDateDom,
        createTableDom,
        createButtonDom,
        createButtonGroupDom,
        createIconButtonDom,
        createHDom,
        createMultiGridDom,
        createNewTableDom,
        createCustomHtmlDom,

        createXmSelectDom,
    }
})