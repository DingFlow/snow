define(["require","assist","settings","global",'BootstrapAsider'],function(require,Assist,Settings,Global,BootstrapAsider){
    let component_properties = {
        data:{
            id:"",//id
            name:'',//name
            cssClass:"row",//css类
            columns:4,//多少列
            columnsArr:[//
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
                },
            ],
            title:"名称",

            uuid:"",
            attributesArr:[],//属性数组
        },
        inputChange:["id","name","cssClass","columns","title"],//input事件修改值
        clickChange:[],
        purview:{//属性编辑权限
            id:3,//id
            name:3,
            cssClass:2,//css类
            columns:3,
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
     * @param {*} moreProps.dropAddComponent 拖拽添加组件
     * @param {*} moreProps.ortumChildren 插入<ortum_children>的data-order
     * @param {*} moreProps.customName 自定义name
     * @param {*} moreProps.nameSuffix 名称后缀
     */
    let GridDom = function(parentDom,moreProps=null){
        let customProps = null;
        // let generateDom =  null;
        let clickChangeAttrs = true;
        let createJson = false;
        let HasProperties = false;
        let dropAddComponent = true;
        let ortumChildren = null;
        let customName = '';//自定义name
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

        let outerDom= $('<div class="ortum_item ortum_bootstrap_grid" data-frame="Bootstrap" data-componentKey="gridDom"></div>');
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
        ortum_component_properties.data.name || (ortum_component_properties.data.name = Assist.timestampName('grid'));
        let nameArr = ortum_component_properties.data.name.split("_");
        if(nameSuffix && createJson){
            ortum_component_properties.data.name = nameArr[0] + "_"+ nameArr[1] + nameSuffix;
        }else{
            ortum_component_properties.data.name = nameArr[0] + "_"+ nameArr[1]
        }


        let row = $(`
            <div class="${ortum_component_properties.data.cssClass}" 
            style="margin: 0"
            ${ortum_component_properties.data.id ? "id="+ortum_component_properties.data.id : ''} 
            ></div>
        `)
        for(let i=0;i<ortum_component_properties.data.columns;i++){
            if(moreProps){
                moreProps.classValue = ortum_component_properties.data.columnsArr[i] && ortum_component_properties.data.columnsArr[i].classValue
            }else{
                moreProps={
                    "classValue" :ortum_component_properties.data.columnsArr[i] && ortum_component_properties.data.columnsArr[i].classValue
                }
            }
            moreProps.ortumChildren = i;
            let col=BootstrapAsider.tipAddComponentFn(true,moreProps)
            $(row).append(col);
        }

        outerDom.append(row);

        //修改编辑的属性
        if(Array.isArray(ortum_component_properties.data.attributesArr)){
            ortum_component_properties.data.attributesArr.forEach(function(item){
                outerDom.find("*[name="+ ortum_component_properties.data.name +"]").attr(item.label,item.value);
            });
        }

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
            case "columns":

                let cloumLength = $(globalComponent).find(".row").eq(0).children().length;
                if(cloumLength > val*1){
                    for(let i =0;i<(cloumLength -(val*1));i++){
                        $(globalComponent).find(".row").eq(0).children().last().remove();
                        evenProperties.data.columnsArr.pop();
                    }
                }
                if(cloumLength < val*1){
                    for(let i =0;i<(val*1 - cloumLength);i++){
                        let col=BootstrapAsider.tipAddComponentFn(true,{ortumChildren:cloumLength+i})
                        $(globalComponent).find(".row").eq(0).append(col)
                        evenProperties.data.columnsArr.push({
                            "classValue":"col"
                        });
                    }
                }
                break; 
            case "cssClass":
                $(globalComponent).find(".row").attr('class',val)
                break; 
            default:
                if(evenProperties.inputChange.indexOf(property) != -1){
                    $(globalComponent).find(".row").eq(0).attr(property,val)
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
                    $(globalComponent).find(".row").eq(0).attr(property,val)
                }
                break;
        }
    }


    /**
     * 功能：新增选项
     * @param {*} newArr
     */
    let setGridItems = function(newArr){
        let globalComponent =Global.ortum_edit_component.comObj;
        let evenProperties = $(globalComponent).prop('ortum_component_properties');
        let colDiv = $(globalComponent).find('.ortum_boot_col_default');

        colDiv.each(function (index,item) {
            $(item).removeClass(function (index, className) {
                return (className.match (/(?<=(^|\s))col(\S)*?(?=($|\s))/g) || []).join(' ');
            })
            $(item).addClass(newArr[index].classValue)
        })

        evenProperties.data.columnsArr = newArr;
    }
    /**
     * 功能：回显选项
     */
    let showGridItems = function(){
        $('#ortum_top_dialog').modal({
            "backdrop":"static",
        });
        $("#ortum_top_model_content").load("/grid_settings/grid_settings",function(){
            let globalComponent =Global.ortum_edit_component.comObj;
            let evenProperties = $(globalComponent).prop('ortum_component_properties');

            let itemsArr = evenProperties.data.columnsArr;
            let itemsLength = itemsArr.length;
            for(let i =1 ;i<itemsLength;i++){
                $('#ortum_grid_addLine').click();
            }
            itemsLength && $('#ortum_grid_ModalLabel .ModalLabelTable').find('.ortum_order_dataTr').each(function(index,item){
                $(item).find('.ortum_grid_colValue').eq(0).val(itemsArr[index].classValue)
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

    /**
     * 根据组件返回子组件的位置
     * @param ortumItemDom
     */
    let getOrtumChildrenOrder = function (ortumItemDom,sonOrtumItem) {
        let ortumChildrenOrder = $(sonOrtumItem).parent().attr("data-order");
        return ortumChildrenOrder
    }

    return {
        GridDom,

        inputSetProperties,
        blurSetProperties,
        // changeSetProperties,
        clickSetProperties,
        // keyDownSetProperties,
        // keyUpSetProperties,

        setGridItems,
        showGridItems,

        ortumComponentSetJs,
        ortumComponentSaveJs,

        getOrtumChildrenOrder,
    }
})