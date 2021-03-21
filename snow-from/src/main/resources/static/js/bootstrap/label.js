define(["require","assist","createDom","global"],function(require,Assist,CreateDom,Global){
    let component_properties = {
        data:{
            id:"",//id
            name:'',//name
            verification:"",//校验

            cssClass:"ortum_bootstrap_label_cssClass",
            labelCSS:"ortum_bootstrap_label_labelCSS",//css类
            labelName:"名称",//标签名称
            title:"",
            showSpan:true,//显示tip
            bindComponentName:"",//关联组件，label的权限由绑定组件一致

            uuid: "",
            attributesArr:[],//属性数组
        },
        inputChange:["id","name","verification","labelCSS","labelName","cssClass","title"],//input事件修改值
        clickChange:["showSpan"],
        changeChange:["bindComponentName"],
        purview:{//属性编辑权限
            id:3,//id
            name:3,
            cssClass:3,
            verification:3,
            labelCSS:3,//css类
            labelName:3,//标签名称
            title:3,
            bindComponentName:3,
            showSpan:3,
        },
        dataShowType:{
            showSpan:"switch",
        },
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
     * @param {*} moreProps.ortumChildren 插入<ortum_children>的data-order
     * @param {*} moreProps.customName 自定义name
     * @param {*} moreProps.nameSuffix 名称后缀
     */
    let LabelDom = function(parentDom,moreProps=null){
        let customProps = null;
        // let generateDom =  null;
        let clickChangeAttrs = true;
        let dropAddComponent = true;
        let customName = '';//自定义name

        let createJson = false;
        let HasProperties = false;
        let ortumChildren = null;
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
            moreProps.nameSuffix !== null && moreProps.nameSuffix !== undefined && (nameSuffix = moreProps.nameSuffix);
        }

        let outerDom=$(
            `
            <div class="ortum_item ortum_bootstrap_label" data-frame="Bootstrap" 
            data-componentKey="labelDom">
               
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
        ortum_component_properties.data.name || (ortum_component_properties.data.name = Assist.timestampName('label'));
        let nameArr = ortum_component_properties.data.name.split("_");
        if(nameSuffix && createJson){
            ortum_component_properties.data.name = nameArr[0] + "_"+ nameArr[1] + nameSuffix;
        }else{
            ortum_component_properties.data.name = nameArr[0] + "_"+ nameArr[1]
        }

        $(outerDom).append($(`
            <div class="${ortum_component_properties.data.cssClass}">
                <span class="ortum_bootstrap_span" ${!ortum_component_properties.data.showSpan ? 'style="display: none"': '' } >*</span>
                <label class="${ortum_component_properties.data.labelCSS}" 
                ${ortum_component_properties.data.name ? "name="+ortum_component_properties.data.name : '' }
                ${ortum_component_properties.data.bindComponentName ? "ortum_bindcomponentname="+ortum_component_properties.data.bindComponentName : '' } 
                ${ortum_component_properties.data.id ? "id="+ortum_component_properties.data.id : '' }>${ortum_component_properties.data.labelName}</label>
            </div>
        `))

        //修改编辑的属性
        if(Array.isArray(ortum_component_properties.data.attributesArr)){
            ortum_component_properties.data.attributesArr.forEach(function(item){
                outerDom.find("*[name="+ ortum_component_properties.data.name +"]").attr(item.label,item.value);
            });
        }


        //dom绑定property
        clickChangeAttrs !== false && $(outerDom).prop('ortum_component_properties',ortum_component_properties).prop('ortum_component_type',['Bootstrap','label']);
        if(parentDom){
            $(parentDom).append(outerDom);
        }else if(createJson){//生成json
            return {
                "name":ortum_component_properties.data.name,
                "html":outerDom[0].outerHTML.replace(/\n/g,'').replace(/(\s)+/g," "),
                "bindComponentName":ortum_component_properties.data.bindComponentName,
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
        switch(property){
            case "verification":
                //TODO 验证
                console.log(val)
                break;
            case "labelName":
                $(globalComponent).find('label').eq(0).text(val)
                break;
            case "labelCSS":
                // $(globalComponent).find('label').eq(0).addClass(val)
                $(globalComponent).find('label').eq(0).attr('class',val)
                break;
            default:
                if(evenProperties.inputChange.indexOf(property) != -1){
                    $(globalComponent).find('label').eq(0).attr(property,val)
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
            case "showSpan":
                evenProperties.data[property] = checked;
                break;
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
            case "showSpan":
                checked && $(globalComponent).find('.ortum_bootstrap_span').eq(0).show();
                !checked && $(globalComponent).find('.ortum_bootstrap_span').eq(0).hide();
                break;
            default:
                if(evenProperties.clickChange.indexOf(property) != -1){
                    $(globalComponent).find('label').eq(0).attr(property,val)
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
            case "bindComponentName":
                evenProperties.data[property] = val;
                val && $(globalComponent).find('label').eq(0).attr("ortum_bindcomponentname",val);
                !val && $(globalComponent).find('label').eq(0).removeAttr("ortum_bindcomponentname");
                break
            default:
                if(evenProperties.changeChange.indexOf(property) != -1){
                    $(globalComponent).find('label').eq(0).attr(property,val)
                }
                break;
        }
    }
    /**
     * 功能：参数设置显示之前的操作
     */
    let beforeSetPrperies = function () {
        //设置关联组件选项
        let optionArr = require('feature').getFormComponentsProps(null,"name");
        $("#ortum_property_bindComponentName").html('')
        if(optionArr.length){
            $("#ortum_property_bindComponentName").append(`<option value="" selected>请选择</option>`)
            optionArr.forEach((item)=>{
                $("#ortum_property_bindComponentName").append(`
                    <option value="${item.text}">${item.title}</option>
                `)
            })
        }else{
            $("#ortum_property_bindComponentName").append(`<option value="" disabled>暂无组件可选</option>`)
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

    return {
        LabelDom,

        inputSetProperties,
        blurSetProperties,
        changeSetProperties,
        clickSetProperties,
        // keyDownSetProperties,
        // keyUpSetProperties,
        beforeSetPrperies,

        ortumComponentSetJs,
        ortumComponentSaveJs,
    }
})