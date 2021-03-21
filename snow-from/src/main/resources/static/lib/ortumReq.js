/**
 * 
 * 自定义XMLHttpRequest
 * 时间：2020年10月19日
 * 
 */

!function(global, factory){
	if ( typeof module === "object" && typeof module.exports === "object" ) {
		module.exports = global.document ?
			factory( global, true ) :
			function(w) {
				if ( !w.document ) {
					throw new Error( "ortumReq requires a window with a document" );
				}
				return factory(w);
			};
	} else {
		factory(global);
	}
}(typeof window !== "undefined" ? window : this,function( window) {
    var version  = "0.0.1";
    var ortumReq = function( jsonObj) {
		return new ortumReq.fn.init(jsonObj);
    };

    
    ortumReq.fn = ortumReq.prototype = {
        // The current version of jQuery being used
        ortumReq: version,
    
        constructor: ortumReq,
    
        // Start with an empty selector
        selector: "",
    
        length: 0,
    };

    

    ortumReq.ortumReqSettings = {
        timeout:'',//请求时间
        successBefore:null,//onload时，处理之前,返回true，终止执行
        successAfter:null,//onload时，处理之后,
        errorBefore:null,//onerror时，处理之前,返回true，终止执行
        errorAfter:null,
        header:{},//统一的请求头
    }

    //设置ortumReqSettings
    ortumReq.ortumReqSetup = function(target){
        if(Object.prototype.toString.call(target).slice(8,-1) == "Object"){
            for(var key in this.ortumReqSettings){
                if(target[key] !== undefined){
                    this.ortumReqSettings[key] =  target[key]
                }
            }
        }
    };


    ortumReq.fn.init = function(obj = {}){
        if(Object.prototype.toString.call(obj) != "[object Object]"){
            console.error("必须传入object对象");
            return;
        }

        let method = obj.method || "GET";
        let url = obj.url;
        let async = obj.async === false ? false : true;
        let header = obj.header ? Object.assign(ortumReq.ortumReqSettings.header,obj.header) : ortumReq.ortumReqSettings.header;
        let timeout = obj.timeout ||  ortumReq.ortumReqSettings.timeout;

        if(!url){
            return console.err('缺少url参数')
        };

        //成功
        let success = typeof obj.success === 'function' ? obj.success : null;
        let successBefore = typeof obj.successBefore == "function" ? obj.successBefore : null;
        let successAfter = typeof obj.successAfter == "function" ? obj.successAfter : null;

        //失败
        let error = typeof obj.error == "function" ? obj.error : null;
        let errorBefore = typeof obj.errorBefore == "function" ? obj.errorBefore : null;
        let errorAfter = typeof obj.errorAfter == "function" ? obj.errorAfter : null;


        let final = typeof obj.final == "function" ? obj.final : null;
        let onstart = typeof obj.onstart == "function" ? obj.onstart : null;
        let ontimeout = typeof obj.ontimeout == "function" ? obj.ontimeout : null;
        let onabort = typeof obj.onabort == "function" ? obj.onabort : null;
        let onprogress = typeof obj.onprogress == "function" ? obj.onprogress : null;

        //数据传输中的监听函数
        let progress = typeof obj.progress == "function" ? obj.progress : null;

        let XHR = new XMLHttpRequest();
        //设置请求时间
        timeout &&  (XHR.timeout = obj.timeout);

        XHR.open(method,url,async);

        //设置请求头
        Object.prototype.toString.call(header) == "[object Object]" && Object.keys(header).forEach(item=>{
            XHR.setRequestHeader(item, header[item]);
        });

        //请求成功完成时
        XHR.onload = function(e){
            let beforePause = false;
            if(successBefore){
                beforePause = successBefore(this,e)
            }else if(ortumReq.ortumReqSettings.successBefore){
                beforePause = ortumReq.ortumReqSettings.successBefore(this,e);
            }
            if(beforePause)return;
            let afterPause = success && success(this,e);

            if(!afterPause && successAfter){
                successAfter(this,e)
            }else if(!afterPause && ortumReq.ortumReqSettings.successAfter){
                ortumReq.ortumReqSettings.successAfter(this,e)
            }
        };
        //遭遇错误时（失败）
        XHR.onerror =function(e){
            let beforePause = false;
            if(errorBefore){
                beforePause = errorBefore(this,e)
            }else if(ortumReq.ortumReqSettings.errorBefore){
                beforePause = ortumReq.ortumReqSettings.errorBefore(this,e);
            }
            if(beforePause)return;
            let afterPause = error && error(this,e);
            if(!afterPause && errorAfter){
                errorAfter(this,e)
            }else if(!afterPause && ortumReq.ortumReqSettings.errorAfter){
                ortumReq.ortumReqSettings.errorAfter(this,e)
            }
        };

        //被停止时
        XHR.onabort = function(e){
            typeof onabort === 'function' && onabort(this,e)
        };
        //请求接收(无论是否成功)
        XHR.onloadend = function (e) {
            typeof final === 'function' && final(this,e)
        };
        //接收到响应数据时
        XHR.onloadstart = function (e) {
            typeof onstart === 'function' && onstart(this,e)
        };
        //当请求接收到更多数据时，周期性地触发
        XHR.onprogress = function (e) {
            typeof onprogress === 'function' && onprogress(this,e)
        };
        //超时
        XHR.ontimeout =function (e) {
            typeof ontimeout === 'function' && ontimeout(this,e)
        };


        XHR.upload.onprogress = function (e) {
            typeof progress === 'function' && progress(this,e)
        };

        //发起
        obj.data ? XHR.send(obj.data) : XHR.send(obj.data);
        return XHR;
    };

    window.ortumReq = ortumReq;
    return ortumReq;
})



