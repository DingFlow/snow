define(["require","assist","createDom","global"],function(require,Assist,CreateDom,Global){
    let component_properties = {
        data:{
            // id:"",//id
            name:'',//name
            defaultVal:"option2",//默认值
            verification:"",//校验
            authority:"edit",//权限
            // placeholder:"请输入",
            cssClass:"form-check-input",//css类
            // hideLabel:false,//是否隐藏标签
            // labelName:"名称",//标签名称
            // labelPosition:"rowLeft",//标签位置
            // labelWidth:"",//标签宽度
            labelCSS:"form-check-label",//标签css类,
            inline:true,//单行显示
            title:"名称",//
            items:[
                {
                    "label":"香水",
                    "value":"option1",
                    "data-id":"miao",
                },
                {
                    "label":"玫瑰",
                    "value":"option2",
                    "data-id":"hao",
                },
            ],
            uuid: "",
            attributesArr:[],//属性数组
        },
        inputChange:["name","defaultVal","verification","cssClass","labelCSS","title"],//input事件修改值
        clickChange:["authority","inline"],
        purview:{//属性编辑权限
            // id:1,//id
            name:3,
            defaultVal:3,
            verification:3,
            authority:3,//权限
            // placeholder:3,
            cssClass:3,//css类
            // hideLabel:3,//是否隐藏标签
            // labelName:3,//标签名称
            // labelPosition:3,//标签位置
            // labelWidth:1,//标签宽度
            labelCSS:3,//标签css类
            // inline:3,//单行显示
            title:3,
        },
        dataShowType:{
            // inline:'switch',
            authority:"checkbox",
        },
    }

    /**
     * 功能：创建bootstrap的input
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
    let RadioDom = function(parentDom,moreProps=null){
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
            moreProps.dropAddComponent === false && (dropAddComponent = moreProps.dropAddComponent);
            moreProps.ortumChildren !== null && moreProps.ortumChildren !== undefined && (ortumChildren = moreProps.ortumChildren);
            moreProps.customName !== null && moreProps.customName !== undefined && (customName =moreProps.customName);
            moreProps.nameSuffix !== null && moreProps.nameSuffix !== undefined && (nameSuffix = moreProps.nameSuffix);
        }

        let outerDom=$(
            `
            <div class="form-group ortum_item ortum_bootstrap_radio" data-frame="Bootstrap" 
            data-componentKey="radioDom">
               
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
        ortum_component_properties.data.name || (ortum_component_properties.data.name = Assist.timestampName('radio'));
        let nameArr = ortum_component_properties.data.name.split("_");
        if(nameSuffix && createJson){
            ortum_component_properties.data.name = nameArr[0] + "_"+ nameArr[1] + nameSuffix;
        }else{
            ortum_component_properties.data.name = nameArr[0] + "_"+ nameArr[1]
        }


        for(let i=0;i<ortum_component_properties.data.items.length;i++){
            let choose = false;
            if(ortum_component_properties.data.defaultVal == ortum_component_properties.data.items[i].value){
                choose = true
            }
            let newDom = $(`
            <div class="form-check ${ortum_component_properties.data.inline?'form-check-inline':''}">
                <input class="${ortum_component_properties.data.cssClass}" ${choose ? "checked" :""} type="radio" name="${ortum_component_properties.data.name}" id="${ortum_component_properties.data.name+"_"+i}" value="${ortum_component_properties.data.items[i].value}">
                <label class="${ortum_component_properties.data.labelCSS}" for="${ortum_component_properties.data.name+"_"+i}">
                    ${ortum_component_properties.data.items[i].label}
                </label>
            </div>
            `);
            let obj =ortum_component_properties.data.items[i]
            for (key in obj) {
                if (obj.hasOwnProperty(key) && key != "label" && key != 'value') {
                    $(newDom).find('.form-check-input').eq(0).attr(key,obj[key])
                }
            }
            $(outerDom).append(newDom)
        };

        //修改编辑的属性
        if(Array.isArray(ortum_component_properties.data.attributesArr)){
            ortum_component_properties.data.attributesArr.forEach(function(item){
                outerDom.find("*[name="+ ortum_component_properties.data.name +"]").attr(item.label,item.value);
            });
        }

        //TODO 后渲染的关联组件，可能无法正常控制权限
        switch (ortum_component_properties.data.authority) {
            case "hide":
                $(outerDom).addClass("ortum_display_NONE");
                // $("*[ortum_bindcomponentname="+ ortum_component_properties.data.name +"]").parents(".ortum_item").eq(0).addClass("ortum_display_NONE");
                break;
            case "edit":
                $(outerDom).removeClass("ortum_display_NONE");
                $(outerDom).find("input").removeAttr("readonly");
                $(outerDom).find("input").removeAttr("disabled");
                // $("*[ortum_bindcomponentname="+ ortum_component_properties.data.name +"]").parents(".ortum_item").eq(0).removeClass("ortum_display_NONE");
                break;
            case "readonly":
                $(outerDom).removeClass("ortum_display_NONE");
                $(outerDom).find("input").attr("readonly","readonly");
                $(outerDom).find("input").removeAttr("disabled");
                // $("*[ortum_bindcomponentname="+ ortum_component_properties.data.name +"]").parents(".ortum_item").eq(0).removeClass("ortum_display_NONE");
                break;
            case "disabled":
                $(outerDom).removeClass("ortum_display_NONE");
                $(outerDom).find("input").attr("readonly","readonly");
                $(outerDom).find("input").attr("disabled","disabled");
                // $("*[ortum_bindcomponentname="+ ortum_component_properties.data.name +"]").parents(".ortum_item").eq(0).removeClass("ortum_display_NONE");
                break;
            default:
                break;
        };


        //dom绑定property
        clickChangeAttrs !== false && $(outerDom).prop('ortum_component_properties',ortum_component_properties).prop('ortum_component_type',['Bootstrap','radio']);
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
        let val=$(that).val();
        let checked=$(that).prop('checked');

        if(!Global.ortum_edit_component || !Global.ortum_edit_component.comObj){
            return false;
        }
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');

        //判断值是否合理
        let vertifyPause =  evenProperties.verify && evenProperties.verify[property] && evenProperties.verify[property]['input'] && evenProperties.verify[property]["input"](globalComponent,e);
        if(vertifyPause){
            return false;
        }
        //更新到dom属性上
        // evenProperties.data[property] = val;
        switch(property){
            case "defaultVal":
                // $(globalComponent).find('input').removeAttr('checked').removeProp("checked");
                $(globalComponent).find('input[value='+ val+']').eq(0).attr('checked',true);
                break;
            case "verification":
                //TODO 验证
                console.log(val)
                break;
            case "cssClass":
                // $(globalComponent).find('input.form-control').eq(0).addClass(val)
                $(globalComponent).find('input').attr('class',val)
                break; 
            case "labelCSS":
                // $(globalComponent).find('label').eq(0).addClass(val)
                $(globalComponent).find('label').attr('class',val)
                break;  
            default:
                if(evenProperties.inputChange.indexOf(property) != -1){
                    $(globalComponent).find('input').eq(0).attr(property,val)
                }
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
            case "authority":
                if(val=="hide"){//不可见
                    $(globalComponent).addClass("ortum_display_NONE");
                    // $("*[ortum_bindcomponentname="+ evenProperties.data.name +"]").parents(".ortum_item").eq(0).addClass("ortum_display_NONE");
                }
                if(val=="edit"){//可编辑
                    $(globalComponent).removeClass("ortum_display_NONE");
                    $(globalComponent).find("input").removeAttr("readonly");
                    $(globalComponent).find("input").removeAttr("disabled");
                    // $("*[ortum_bindcomponentname="+ evenProperties.data.name +"]").parents(".ortum_item").eq(0).removeClass("ortum_display_NONE");
                }
                if(val=="readonly"){//只读可点击
                    $(globalComponent).removeClass("ortum_display_NONE");
                    $(globalComponent).find("input").attr("readonly","readonly");
                    $(globalComponent).find("input").removeAttr("disabled");
                    // $("*[ortum_bindcomponentname="+ evenProperties.data.name +"]").parents(".ortum_item").eq(0).removeClass("ortum_display_NONE");
                }
                if(val=="disabled"){//只读且无法点击
                    $(globalComponent).removeClass("ortum_display_NONE");
                    $(globalComponent).find("input").attr("readonly","readonly");
                    $(globalComponent).find("input").attr("disabled","disabled");
                    // $("*[ortum_bindcomponentname="+ evenProperties.data.name +"]").parents(".ortum_item").eq(0).removeClass("ortum_display_NONE");
                }
                break;
            case "inline":
                if(checked){
                    $(globalComponent).find('.form-check').addClass('form-check-inline');
                }else{
                    $(globalComponent).find('.form-check').removeClass('form-check-inline');
                }
                break;    
            default:
                if(evenProperties.clickChange.indexOf(property) != -1){
                    $(globalComponent).find('input.form-control').eq(0).attr(property,val)
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
        //更新到dom属性上
        switch(property){
            case "inline":
                evenProperties.data[property] = checked;
                break;
            default:
                evenProperties.data[property] = val;
                break;
        }
    }

    /**
     * 功能：新增选项
     * @param {*} newArr 
     */
    let setRadioItems = function(newArr){
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');
        $(globalComponent).find('.form-check').remove();

        for(let i=0;i<newArr.length;i++){
            let choose = false;
            if(evenProperties.data.defaultVal == newArr[i].value){
                choose = true
            }
            let newDom = $(`
            <div class="form-check ${evenProperties.data.inline?'form-check-inline':''}">
                <input class="${evenProperties.data.cssClass}" ${choose ? "checked" :""} type="radio" name="${evenProperties.data.name}" id="${evenProperties.data.name+"_"+i}" value="${newArr[i].value}">
                <label class="${evenProperties.data.labelCSS}" for="${evenProperties.data.name+"_"+1}">
                    ${newArr[i].label}
                </label>
            </div>
            `);
            let obj =newArr[i];
            for (key in obj) {
                if (obj.hasOwnProperty(key) && key != "label" && key != 'value') {
                    $(newDom).find('.form-check-input').eq(0).attr(key,obj[key])
                }
            }
            $(globalComponent).append(newDom)
        }
        evenProperties.data.items = newArr;
    }
    /**
     * 功能：回显选项
     */
    let showRadioItems = function(){
        
        $('#ortum_top_dialog').modal({
            "backdrop":"static",
        })
        $("#ortum_top_model_content").load("/from/interface/radio_settings",function(){
            let globalComponent =Global.ortum_edit_component.comObj;
            let evenProperties = $(globalComponent).prop('ortum_component_properties');

            let itemsArr = evenProperties.data.items;
            let itemsLength = itemsArr.length;
            for(let i =1 ;i<itemsLength;i++){
                $('#ortum_radio_addLine').click();
            }
            itemsLength && $('#ortum_radio_ModalLabel .ModalLabelTable').find('.ortum_order_dataTr').each(function(index,item){
                $(item).find('.ortum_radio_label').eq(0).val(itemsArr[index].label)
                $(item).find('.ortum_radio_value').eq(0).val(itemsArr[index].value)
            })

            $('#ortum_top_model_wait').hide();
        });
        return false;
    };

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
        RadioDom,

        setRadioItems,
        showRadioItems,

        inputSetProperties,
        blurSetProperties,
        // changeSetProperties,
        clickSetProperties,
        // keyDownSetProperties,
        // keyUpSetProperties,

        ortumComponentSetJs,
        ortumComponentSaveJs,

    }
})