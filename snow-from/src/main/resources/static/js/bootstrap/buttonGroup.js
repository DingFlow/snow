define(["require","assist","createDom","global","settings"],function(require,Assist,CreateDom,Global,Settings){
    let component_properties = {
        data:{
            id:"",//id
            name:'',//name
            verification:"",//校验
            cssClass:"ortum_bootstrap_buttonGroup_div ortum_append",
            title:"",
            childrenSlot:0,//子button的个数

            uuid: "",
            attributesArr:[],//属性数组
        },
        inputChange:["id","name","verification","cssClass","title"],//input事件修改值
        clickChange:[],
        changeChange:[],
        purview:{//属性编辑权限
            id:3,//id
            name:3,
            cssClass:3,
            verification:3,
            title:3,
        },
        dataShowType:{
        },
    }

    /**
     * grid的拖拽事件
     * @param {*} ele 
     */
    let bindDropEventToButtonGroup = function(ele){
        $(ele).on('dragover.firstbind',function(e){
            return false;
        });

        $(ele).on("dragenter.firstbind",function(e){//有拖动对象(包括自己作为拖动对象)进入我的领空时
            //获取要创建的组件key
            let componentKey = $(Global.ortumNowDragObj).attr('data-key');
            //拖拽的是绘制区的组件
            let hasOrtumItem = $(Global.ortumNowDragObj).hasClass("ortum_item");
            if(hasOrtumItem){
                if($(Global.ortumNowDragObj).hasClass("ortum_bootstrap_button") || $(Global.ortumNowDragObj).hasClass("ortum_bootstrap_iconButton")){
                    require("feature").ortumDragShadow(e,"enter",{That:this,addWay:"addClass"})
                    return false;
                }
            }else if(componentKey && (componentKey == "buttonDom" || componentKey == "iconButtonDom")){
                require("feature").ortumDragShadow(e,"enter",{That:this,addWay:"addClass"})
                return false;
            }
        });

        $(ele).on('drop.firstbind',function(e){
            require("feature").ortumDragShadow(e,"drop",{That:this});
            if(!Global.ortumNowDragObj){
                return false;
            }
            
            //获取要创建的组件key
            let componentKey = $(Global.ortumNowDragObj).attr('data-key');
            //拖拽的是绘制区的组件
            let hasOrtumItem = $(Global.ortumNowDragObj).hasClass("ortum_item");

            if(hasOrtumItem){
                if($(Global.ortumNowDragObj).hasClass("ortum_bootstrap_button") || $(Global.ortumNowDragObj).hasClass("ortum_bootstrap_iconButton")){
                    let btnDom =$(Global.ortumNowDragObj);
                    $(this).append(btnDom);
                    return false;
                }
            }else{
                if(!componentKey){//不存在对应key
                    return false;
                }
                if(!require('createDom')[Settings.menuListDataJSON[componentKey].createFn]){
                    Assist.dangerTip();
                    return false;
                }
                if(componentKey == "buttonDom" || componentKey == "iconButtonDom"){
                    let onlyFrame = $(Global.ortumNowDragObj).attr("data-frame");
                    let useFrame = onlyFrame || Global.ortum_createDom_frame

                    //执行对应的生成组件的函数(此处要解决 grid.js 与createDom 循环依赖的问题)
                    let btnDom =require('createDom')[Settings.menuListDataJSON[componentKey].createFn](null,useFrame)
                    $(this).append(btnDom);
    
                    //把拖拽对象制空
                    Global.ortumNowDragObj = null;
    
                    return false;
                }
            } 
        });
    }

    /**
     * 功能：创建bootstrap的label
     * @param {*} parentDom
     * @param {*} moreProps 一个json对象，
     * @param {*} moreProps.customProps 自定义属性
     * @param {*} moreProps.generateDom 函数也存在dom中
     * @param {*} moreProps.createJson 生成对应的json
     * @param {*} moreProps.HasProperties 保存组件的component_properties
     * @param {*} moreProps.clickChangeAttrs 是否允许修改点击属性（=== false的时候，去除点击修改属性）
     * @param {*} moreProps.dropAddComponent 拖拽添加组件
     * @param {*} moreProps.customName 自定义name
     * @param {*} moreProps.ortumChildren 插入<ortum_children>的data-order
     * @param {*} moreProps.ortumItemDom 编辑器中item对象
     * @param {*} moreProps.nameSuffix 名称后缀
     */
    let ButtonGroupDom = function(parentDom,moreProps=null){
        let customProps = null;
        // let generateDom =  null;
        let clickChangeAttrs = true;
        let dropAddComponent = true;
        let customName = '';//自定义name

        let createJson = false;
        let HasProperties = false;
        let ortumChildren = null;
        let ortumItemDom = null;
        let nameSuffix = null;

        if(Assist.getDetailType(moreProps) == "Object"){
            customProps = (Assist.getDetailType(moreProps.customProps) == "Object" ? moreProps.customProps : null);
            // moreProps.generateDom !== null && moreProps.generateDom !== undefined && (generateDom =moreProps.generateDom);
            moreProps.clickChangeAttrs === false && (clickChangeAttrs = moreProps.clickChangeAttrs);
            moreProps.HasProperties !== null && moreProps.HasProperties !== undefined && (HasProperties =moreProps.HasProperties);
            moreProps.createJson !== null && moreProps.createJson !== undefined && (createJson =moreProps.createJson);
            moreProps.customName !== null && moreProps.customName !== undefined && (customName =moreProps.customName);
            moreProps.dropAddComponent === false && (dropAddComponent = moreProps.dropAddComponent);
            moreProps.ortumChildren !== null && moreProps.ortumChildren !== undefined && (ortumChildren = moreProps.ortumChildren);
            moreProps.ortumItemDom !== null && moreProps.ortumItemDom !== undefined && (ortumItemDom =moreProps.ortumItemDom);
            moreProps.nameSuffix !== null && moreProps.nameSuffix !== undefined && (nameSuffix = moreProps.nameSuffix);
        }

        let outerDom=$(
            `
            <div class="ortum_item ortum_bootstrap_buttonGroup" data-frame="Bootstrap" 
            data-componentKey="buttonGroupDom">
               
            </div>
            `
        );
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
        ortum_component_properties.data.name || (ortum_component_properties.data.name = Assist.timestampName('buttonGroup'));

        let nameArr = ortum_component_properties.data.name.split("_");
        if(nameSuffix && createJson){
            ortum_component_properties.data.name = nameArr[0] + "_"+ nameArr[1] + nameSuffix;
        }else{
            ortum_component_properties.data.name = nameArr[0] + "_"+ nameArr[1]
        }

        let divGroup = $(`
            <div ${ortum_component_properties.data.id ? "id="+ortum_component_properties.data.id : '' } 
            name="${ortum_component_properties.data.name}" 
            class="${ortum_component_properties.data.cssClass}" >

            </div>
        `);
        //修改编辑的属性
        if(Array.isArray(ortum_component_properties.data.attributesArr)){
            ortum_component_properties.data.attributesArr.forEach(function(item){
                outerDom.find("*[name="+ ortum_component_properties.data.name +"]").attr(item.label,item.value);
            });
        }

        //拖拽组件
        dropAddComponent !== false && bindDropEventToButtonGroup(divGroup);
        dropAddComponent !== false && $(divGroup).addClass("ortum_bootstrap_buttonGroup_drop_div");
        createJson && $(divGroup).addClass("ortum_append");//添加组件

        if(createJson){
            if(ortumItemDom){
                ortum_component_properties.data.childrenSlot = $(ortumItemDom).find(".ortum_item").length;
            }else{
                ortum_component_properties.data.childrenSlot = 0;
            }
            let slotNum = ortum_component_properties.data.childrenSlot;
            while (slotNum>0){
                $(divGroup).append(`<ortum_children data-order=${ortum_component_properties.data.childrenSlot-slotNum}></ortum_children>`)
                slotNum--;
            };
        }else{
            if(!ortum_component_properties.data.childrenSlot){
                ortum_component_properties.data.childrenSlot = 0;
            }
            let slotNum = ortum_component_properties.data.childrenSlot;
            while (slotNum>0){
                $(divGroup).append(`<div data-order=${ortum_component_properties.data.childrenSlot-slotNum}></div>`)
                slotNum--;
            };
        }


        $(outerDom).append(divGroup)

        //dom绑定property
        clickChangeAttrs !== false && $(outerDom).prop('ortum_component_properties',ortum_component_properties).prop('ortum_component_type',['Bootstrap','buttonGroup']);

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
    };
    /**
     * 功能：input事件，在这个事件上重置组件属性
     * @param {*} property 
     * @param {*} val 
     * @param {*} e 
     */
    let inputSetProperties = function(property,that,e){
        let val=$(that).val();
        let checked=$(that).prop('checked');

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
            case "defaultVal":
                $(globalComponent).find('.btn').eq(0).text(val)
                break;
            case "verification":
                //TODO 验证
                console.log(val)
                break;
            case "cssClass":
                $(globalComponent).find('.btn').eq(0).attr('class',val).addClass("ortum_bootstrap_buttonGroup_drop_div")
                break; 
            default:
                if(evenProperties.inputChange.indexOf(property) != -1){
                    $(globalComponent).find('.btn').eq(0).attr(property,val)
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
        // evenProperties.data[property] = val;
        switch(property){
            default:
                if(evenProperties.clickChange.indexOf(property) != -1){
                    $(globalComponent).find('.btn').eq(0).attr(property,val)
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
     * 根据组件返回子组件的位置
     * @param ortumItemDom
     */
    let getOrtumChildrenOrder = function (ortumItemDom,sonOrtumItem) {
        let ortumChildrenOrder = $(sonOrtumItem).index();
        return ortumChildrenOrder;
    }

    return {
        ButtonGroupDom,

        inputSetProperties,
        blurSetProperties,
        // changeSetProperties,
        clickSetProperties,
        // keyDownSetProperties,
        // keyUpSetProperties,

        ortumComponentSetJs,
        ortumComponentSaveJs,
        getOrtumChildrenOrder,
    }
})