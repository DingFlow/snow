define(["require","assist","settings","global",'BootstrapAsider'],function(require,Assist,Settings,Global,BootstrapAsider){
    let component_properties = {
        data:{
            id:"",//id
            name:'',//name
            cssClass:"row",//css类
            gridArr:[
                [
                    {
                        "classValue":"col-2",
                    },
                    {
                        "classValue":"col",
                    },
                    {
                        "classValue":"col-2",
                    },
                    {
                        "classValue":"col",
                    }
                ],
                [
                    {
                        "classValue":"col-2",
                    },
                    {
                        "classValue":"col",
                    },
                    {
                        "classValue":"col-2",
                    },
                    {
                        "classValue":"col",
                    }
                ]
            ],
            title:"名称",

            uuid:"",
            attributesArr:[],//属性数组
        },
        inputChange:["id","name","cssClass","title"],//input事件修改值
        clickChange:[],
        purview:{//属性编辑权限
            id:3,//id
            name:3,
            cssClass:2,//css类
            title:3,
        },
    }
    /**
     * 功能：创建bootstrap的栅格系统
     * @param {*} moreProps.customProps 自定义属性
     * @param {*} moreProps.generateDom 函数转化为String，保存到script标签中
     * @param {*} moreProps.classValue 一个col的宽度，例如 col-2，col-3
     * @param {*} moreProps.createJson 生成对应的json
     * @param {*} moreProps.HasProperties 保存组件的component_properties
     * @param {*} moreProps.clickChangeAttrs 是否允许修改点击属性（=== false的时候，去除点击修改属性）
     * @param {*} moreProps.ortumChildren 插入<ortum_children>的data-order
     * @param {*} moreProps.dropAddComponent 拖拽添加组件
     * @param {*} moreProps.customName 自定义name
     * @param {*} moreProps.nameSuffix 名称后缀
     */
    let MultiGridDom = function(parentDom,moreProps=null){
        let customProps = null;
        // let generateDom =  null;
        let clickChangeAttrs = true;
        let createJson = false;
        let HasProperties = false;
        let ortumChildren = null;
        let customName = '';//自定义name

        let dropAddComponent = true;
        let nameSuffix = null;

        if(Assist.getDetailType(moreProps) == "Object"){
            customProps = (Assist.getDetailType(moreProps.customProps) == "Object" ? moreProps.customProps : null);
            // moreProps.generateDom !== null && moreProps.generateDom !== undefined && (generateDom =moreProps.generateDom);
            moreProps.createJson !== null && moreProps.createJson !== undefined && (createJson =moreProps.createJson);
            moreProps.HasProperties !== null && moreProps.HasProperties !== undefined && (HasProperties =moreProps.HasProperties);
            moreProps.ortumChildren !== null && moreProps.ortumChildren !== undefined && (ortumChildren = moreProps.ortumChildren);
            moreProps.customName !== null && moreProps.customName !== undefined && (customName =moreProps.customName);
            moreProps.dropAddComponent === false && (dropAddComponent = moreProps.dropAddComponent);

            moreProps.clickChangeAttrs === false && (clickChangeAttrs = moreProps.clickChangeAttrs);
            moreProps.nameSuffix !== null && moreProps.nameSuffix !== undefined && (nameSuffix = moreProps.nameSuffix);
        }

        let outerDom= $('<div class="ortum_item ortum_bootstrap_multiGrid" data-frame="Bootstrap" data-componentKey="multiGridDom"></div>');
        //点击事件，修改属性
        clickChangeAttrs !== false && $(outerDom).off('click.addClickChoose').on('click.addClickChoose',Assist.addClickChoose);
        //拖拽事件
        dropAddComponent !== false && require("feature").bindDropEventToOrtumItem(outerDom);

        let ortum_component_properties = customProps ? customProps : Assist.deepClone(component_properties);

        //生成uuid
        ortum_component_properties.data.uuid || (ortum_component_properties.data.uuid = Assist.getUUId());
        outerDom.attr("ortum_uuid",ortum_component_properties.data.uuid)
        //设定name
        customName && (ortum_component_properties.data.name = customName);
        ortum_component_properties.data.name || (ortum_component_properties.data.name = Assist.timestampName('multiGrid'));
        let nameArr = ortum_component_properties.data.name.split("_");
        if(nameSuffix && createJson){
            ortum_component_properties.data.name = nameArr[0] + "_"+ nameArr[1] + nameSuffix;
        }else{
            ortum_component_properties.data.name = nameArr[0] + "_"+ nameArr[1];
        }

        ortum_component_properties.data.id && outerDom.attr("id", ortum_component_properties.data.id);

        let rowDomArr = [];
        ortum_component_properties.data.gridArr.forEach(function(rowItem,rowIndex){
            let row = $(`
                <div class="${ortum_component_properties.data.cssClass}" 
                style="margin: 0"></div>
            `);
            rowItem.forEach(function(columnItem,columnIndex){
                if(moreProps){
                    moreProps.classValue = columnItem.classValue
                }else{
                    moreProps={
                        "classValue":columnItem.classValue 
                    }
                }
                moreProps.ortumChildren = rowIndex+"-"+columnIndex;
                let col=BootstrapAsider.tipAddComponentFn(true,moreProps)
                $(row).append(col);
            })
            rowDomArr.push(row);
        });

        rowDomArr.forEach(function(rowItem){
            outerDom.append(rowItem);
        })

        //TODO 修改编辑的属性
        /*
        if(Array.isArray(ortum_component_properties.data.attributesArr)){
            ortum_component_properties.data.attributesArr.forEach(function(item){
                outerDom.find("*[name="+ ortum_component_properties.data.name +"]").attr(item.label,item.value);
            });
        }
        */

        //dom绑定property
        clickChangeAttrs !== false && $(outerDom).prop('ortum_component_properties',ortum_component_properties).prop('ortum_component_type',['Bootstrap','grid']);
        if(parentDom){
            $(parentDom).append(outerDom);
        }else if(createJson){//生成json
            return {
                "name":ortum_component_properties.data.name,
                "html":outerDom[0].outerHTML.replace(/\n/g,'').replace(/(\s)+/g," "),
                "title":(ortum_component_properties.data.title ? ortum_component_properties.data.title : ortum_component_properties.data.labelName),
                "componentProperties":(HasProperties ? Assist.jsonStringify(ortum_component_properties) : undefined),
                "ortumChildren":ortumChildren,
            }
        }else{
            return outerDom
        }
    }


    /**
     * 功能：input事件，在这个事件上重置组件属性
     * @param {*} property 
     * @param {*} val 
     * @param {*} e 
     */
    let inputSetProperties = function(property,that,e){
        let val = $(that).val();
        let checked = $(that).prop('checked');

        if(!Global.ortum_edit_component || !Global.ortum_edit_component.comObj){
            return false;
        }
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');

        //判断值是否合理
        let vertifyPause =  evenProperties.verify && evenProperties.verify[property] && evenProperties.verify[property]['input'] && evenProperties.verify[property]["input"](globalComponent,e,val,checked);
        if(vertifyPause){
            return false;
        }
        //更新到dom属性上
        // evenProperties.data[property] = val;
        switch(property){
            case "cssClass":
                $(globalComponent).find(".row").attr('class',val)
                break; 
            case "id":
                $(globalComponent).attr('id',val);
                break; 
            default:
                if(evenProperties.inputChange.indexOf(property) != -1){
                    $(globalComponent).find(".row").attr(property,val)
                }
                break;
        }
    }
    /**
     * 功能：blur事件
     * @param {*} property 
     * @param {*} val 
     * @param {*} e 
     */
    let blurSetProperties = function(property,that,e){
        let val=$(that).val();
        let checked=$(that).prop('checked');

        if(!Global.ortum_edit_component || !Global.ortum_edit_component.comObj){
            return false;
        }
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');
        
        //判断值是否合理
        let vertifyPause = evenProperties.verify && evenProperties.verify[property] && evenProperties.verify[property]["blur"] && evenProperties.verify[property]["blur"](globalComponent,e,val);
        
        if(vertifyPause){
            return false;
        };

        //更新到dom属性上
        switch(property){
            // case "hideLabel":case "useRemote":
            //     evenProperties.data[property] = checked;
            //     break;
            default:
                evenProperties.data[property] = val;
                break;
        }
        
    }

    /**
     * 功能：click事件
     * @param {*} property 
     * @param {*} val 
     * @param {*} e 
     */
    let clickSetProperties = function(property,that,e){
        let val=$(that).val();
        let checked=$(that).prop('checked');

        if(!Global.ortum_edit_component || !Global.ortum_edit_component.comObj){
            return false;
        }
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');
        
        //判断值是否合理
        let vertifyPause = evenProperties.verify && evenProperties.verify[property] && evenProperties.verify[property]["click"] && evenProperties.verify[property]["click"](globalComponent,e,val);
        if(vertifyPause){
            return false;
        }
        //更新到dom属性上
        switch(property){
            default:
                if(evenProperties.clickChange.indexOf(property) != -1){
                    $(globalComponent).find(".row").attr(property,val)
                }
                break;
        }
    }
    /**
     * 功能：设置js
     */
    let ortumComponentSetJs = function(){
        
    }
    /**
     * 功能：保存js
     */
    let ortumComponentSaveJs = function(val){
        
    };

    /**
     * 保存对multiGrid的列设置
     */
    let saveMultiGridColumns = function (val) {
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');

        let packer = new Packer;
        let valFormat = packer.pack(val, 0, 0);

        try{
            eval(valFormat);
            let editColumnArr = eval("MultiGridClumns");

            let rowDomArr = [];
            editColumnArr.forEach(function(rowItem,rowIndex){
                let row = $(`
                    <div class="${evenProperties.data.cssClass}" 
                    style="margin: 0"></div>
                `);
                rowItem.forEach(function(columnItem,columnIndex){
                    let col=BootstrapAsider.tipAddComponentFn(true,{
                        "ortumChildren": rowIndex+"-"+columnIndex,
                        "classValue":columnItem.classValue
                    });

                    if(columnItem.uuid){
                        let hasOrtumItem = $("*[ortum_uuid="+ columnItem.uuid +"]").eq(0);
                        if(hasOrtumItem.length){
                            col.append(hasOrtumItem)
                        }
                        columnItem.uuid = undefined;
                    }
                    $(row).append(col);
                })
                rowDomArr.push(row);
            });
            $(globalComponent).empty();
            rowDomArr.forEach(function(rowItem){
                $(globalComponent).append(rowItem);
            });

            //替换table上的属性值
            evenProperties.data.gridArr = editColumnArr;

        }catch (e) {
            console.error(e);
            console.error("编辑table的column信息错误");
        }
    };

    let setMultiGridColumns = function(){
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');

        Global.ortum_codemirrorJS_setVal = function(codeObj){
            let columnsArr = [];
            evenProperties.data.gridArr.forEach(function (rowItem,rowIndex) {
                let pushArr = [];
                rowItem.forEach(function (columnItem,columnIndex) {
                    let pushItem = {};
                    for(let key in columnItem){
                        if(columnItem.hasOwnProperty(key)){
                            pushItem[key] = columnItem[key]
                        };
                    };

                    let ortumChildren = rowIndex + "-" + columnIndex;
                    let hasOrtumItem = $(globalComponent).find("*[data-order="+ ortumChildren +"] > .ortum_item").eq(0);
                    if(hasOrtumItem.length){
                        pushItem.uuid = hasOrtumItem.attr("ortum_uuid");
                    }
                    pushArr.push(pushItem);
                });
                columnsArr.push(pushArr)
            });

            //格式化
            let columnsArrStr = js_beautify(JSON.stringify(columnsArr),2);

            codeObj.setValue(`//编辑multiGrid的柵格信息\nvar MultiGridClumns = ${columnsArrStr};
                `)
        }

        $('#ortum_top_dialog_xl').modal({
            "backdrop":"static",
            "keyboard":false,
        });
        //编辑js保存后执行的函数
        Global.ortum_codemirrorJS_save = saveMultiGridColumns;
        $("#ortum_top_model_xl_content").load("/from/interface/codemirror",function(){
            $('#ortum_top_model_xl_wait').hide();
        });
        return false;
    }

    /**
     * 根据组件返回子组件的位置
     * @param ortumItemDom
     */
    let getOrtumChildrenOrder = function (ortumItemDom,sonOrtumItem) {
        let ortumChildrenOrder = $(sonOrtumItem).parent().attr("data-order");
        return ortumChildrenOrder;
    }



    return {
        MultiGridDom,

        inputSetProperties,
        blurSetProperties,
        // changeSetProperties,
        clickSetProperties,
        // keyDownSetProperties,
        // keyUpSetProperties,

        setMultiGridColumns,
        saveMultiGridColumns,


        ortumComponentSetJs,
        ortumComponentSaveJs,

        getOrtumChildrenOrder,
    }
})