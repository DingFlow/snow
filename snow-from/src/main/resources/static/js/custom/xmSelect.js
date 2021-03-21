define(["require","assist","createDom","global"],function(require,Assist,CreateDom,Global){
    let component_properties = {
        data:{
            id:"",//id
            name:'',//name

            cssClass:"xm-select-demo",
            title:"名称",
            uuid: "",
            attributesArr:[],//属性数组

            onBefore:"",//渲染之前的回调
            onAfter:"function(){}",//渲染之后的回调

            //值变化
            onEffect:function(effectData){

            },
            //初次渲染
            onRender:function (xmSelect,dataInfo={},on=null) {
                dataInfo.on = on ;
                return xmSelect.render(dataInfo);
            },
            //更新渲染
            onUpdate:function (xmSelect,ele,dataInfo) {
                xmSelect.get(ele,true).update(dataInfo);
                return xmSelect.get(ele,true);
            },
            //获取值
            getValue:function (xmSelect,ele,type="valueStr") {
                return xmSelect.get(ele,true).getValue();
            },
            //设置值
            setValue:function (xmSelect,ele,dataArr = [],show=false,listenOn = true) {
                return xmSelect.get(ele,true).setValue(dataArr,show,listenOn);
            }
        },
        inputChange:["id","name","cssClass","title"],//input事件修改值
        clickChange:[],
        changeChange:[],
        purview:{//属性编辑权限
            id:3,//id
            name:3,
            cssClass:3,
            title:3,
        },
        dataShowType:{
        },
    }

    /**
     * 功能：创建bootstrap的label
     * @param {*} parentDom
     * @param {*} moreProps 一个json对象，
     * @param {*} moreProps.customProps 自定义属性
     * @param {*} moreProps.createJson 生成对应的json
     * @param {*} moreProps.HasProperties 保存组件的component_properties
     * @param {*} moreProps.clickChangeAttrs 是否允许修改点击属性（=== false的时候，去除点击修改属性）
     * @param {*} moreProps.dropAddComponent 拖拽添加组件
     * @param {*} moreProps.ortumChildren 插入<ortum_children>的data-order
     * @param {*} moreProps.customName 自定义name
     * @param {*} moreProps.nameSuffix 名称后缀
     */
    let XmSelectDom = function(parentDom,moreProps=null){
        let customProps = null;
        let clickChangeAttrs = true;
        let dropAddComponent = true;
        let customName = '';//自定义name

        let createJson = false;
        let HasProperties = false;
        let ortumChildren = null;
        let nameSuffix = null;

        if(Assist.getDetailType(moreProps) == "Object"){
            customProps = (Assist.getDetailType(moreProps.customProps) == "Object" ? moreProps.customProps : null);
            moreProps.clickChangeAttrs === false && (clickChangeAttrs = moreProps.clickChangeAttrs);
            moreProps.HasProperties !== null && moreProps.HasProperties !== undefined && (HasProperties =moreProps.HasProperties);
            moreProps.createJson !== null && moreProps.createJson !== undefined && (createJson =moreProps.createJson);
            moreProps.customName !== null && moreProps.customName !== undefined && (customName =moreProps.customName);
            moreProps.dropAddComponent === false && (dropAddComponent = moreProps.dropAddComponent);
            moreProps.ortumChildren !== null && moreProps.ortumChildren !== undefined && (ortumChildren = moreProps.ortumChildren);
            moreProps.nameSuffix !== null && moreProps.nameSuffix !== undefined && (nameSuffix = moreProps.nameSuffix);
        }

        let outerDom=$(
            `
            <div class="ortum_item ortum_custom_xmSelect" data-frame="Custom" 
            data-componentKey="xmSelectDom">
               
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
        outerDom.attr("ortum_uuid",ortum_component_properties.data.uuid);
        //设定name
        customName && (ortum_component_properties.data.name = customName);
        ortum_component_properties.data.name || (ortum_component_properties.data.name = Assist.timestampName('xmSelect'));
        let nameArr = ortum_component_properties.data.name.split("_");
        if(nameSuffix && createJson){
            ortum_component_properties.data.name = nameArr[0] + "_"+ nameArr[1] + nameSuffix;
        }else{
            ortum_component_properties.data.name = nameArr[0] + "_"+ nameArr[1];
        }

        let xmSelectWrap = $(`
            <div ortum-id="xmSelect" ${ortum_component_properties.data.id ? "id="+ortum_component_properties.data.id+"_wrap" : ''} 
                ${ortum_component_properties.data.title ? "title="+ortum_component_properties.data.title : ''} 
            >
                <div id="${ortum_component_properties.data.id}" class="${ortum_component_properties.data.cssClass}"></div>
            </div>

        `);

        $(outerDom).append(xmSelectWrap)

        //修改编辑的属性
        // if(Array.isArray(ortum_component_properties.data.attributesArr)){
        //     ortum_component_properties.data.attributesArr.forEach(function(item){
        //         outerDom.find("*[name="+ ortum_component_properties.data.name +"]").attr(item.label,item.value);
        //     });
        // }

        //scriptDom
        let scriptDom ='';
        if(createJson){
            scriptDom = $(`<script>
                    ${(ortum_component_properties.data.onAfter && typeof ortum_component_properties.data.onAfter === "function") ? '!'+ortum_component_properties.data.onAfter+'("'+ ortum_component_properties.data.name +'");' : ''}
                </script>`);
        }

        //dom绑定property
        clickChangeAttrs !== false && $(outerDom).prop('ortum_component_properties',ortum_component_properties).prop('ortum_component_type',['Bootstrap','customHtml']);
        if(parentDom){
            $(parentDom).append(outerDom);
        }else if(createJson){//生成json
            return {
                "name":ortum_component_properties.data.name,
                "html":outerDom[0].outerHTML.replace(/\n/g,'').replace(/(\s)+/g," "),
                "title":(ortum_component_properties.data.title ? ortum_component_properties.data.title : ortum_component_properties.data.labelName),
                "componentProperties":(HasProperties ? Assist.jsonStringify(ortum_component_properties) : undefined),
                "ortumChildren":ortumChildren,
                "script":scriptDom[0].outerHTML.replace(/\n/g,'').replace(/(\s)+/g," ").length >= 20 ? scriptDom[0].outerHTML.replace(/\n/g,'').replace(/(\s)+/g," ") : '',
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
        switch(property){
            default:
                if(evenProperties.inputChange.indexOf(property) != -1){
                    $(globalComponent).find('*[ortum-id=xmSelect]').eq(0).attr(property,val)
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
                    $(globalComponent).find('*[ortum-id=xmSelect]').eq(0).attr(property,val)
                }
                break;
        }
    }

    /**
     * 功能：change事件
     * @param {*} property
     * @param {*} val
     * @param {*} e
     */
    let changeSetProperties = function(property,that,e){
        let val= $(that).val();
        let checked= $(that).prop('checked');

        if(!Global.ortum_edit_component || !Global.ortum_edit_component.comObj){
            return false;
        }
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');

        //判断值是否合理
        let vertifyPause = evenProperties.verify && evenProperties.verify[property] && evenProperties.verify[property]["change"] && evenProperties.verify[property]["change"](globalComponent,e,val);
        if(vertifyPause){
            return false;
        }
        switch(property){
            default:
                if(evenProperties.changeChange.indexOf(property) != -1){
                    $(globalComponent).find('*[ortum-id=xmSelect]').eq(0).attr(property,val)
                }
                break;
        }
    }


    /**
     * 功能：设置js
     */
    let ortumComponentSetJs = function(codeObj){
        if(!Global.ortum_edit_component || !Global.ortum_edit_component.comObj){
            return false;
        }
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');

        let setStr = "var ortum_BootstrapInput_setJs = {";
        if(evenProperties.data.onBefore){
            setStr += "\n//DOM渲染前的执行函数\nonBefore:"+ evenProperties.data.onBefore.toString() + ",";
        }else{
            setStr += "\n//DOM渲染前的执行函数\nonBefore:function(){},"
        }
        if(evenProperties.data.onAfter){
            setStr += "\n//DOM渲染后的执行函数\nonAfter:"+ evenProperties.data.onAfter.toString() + ",";
        }else{
            setStr += "\n//DOM渲染后的执行函数\nonAfter:function(name){},"
        }
        setStr +="\n};";

        //格式化
        setStr = js_beautify(setStr,2);
        codeObj.setValue(setStr)
    }
    /**
     * 功能：保存js
     */
    let ortumComponentSaveJs = function(val){
        if(!Global.ortum_edit_component || !Global.ortum_edit_component.comObj){
            return false;
        };
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');

        let packer = new Packer;
        let valFormat = packer.pack(val, 0, 0);
        try{
            eval(valFormat);
            evenProperties.data.onBefore = ortum_BootstrapInput_setJs.onBefore;
            evenProperties.data.onAfter = ortum_BootstrapInput_setJs.onAfter;
        }catch (e) {
            console.error(e);
            console.error("设置customHtml的js有误，请重新设置");
        }
    };



    return {
        XmSelectDom,

        inputSetProperties,
        blurSetProperties,
        changeSetProperties,
        clickSetProperties,
        // keyDownSetProperties,
        // keyUpSetProperties,

        ortumComponentSetJs,
        ortumComponentSaveJs,

    }
})