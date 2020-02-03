Code.Clock = {
	isRedHot: false,
	init : function(timeOpenedHtml, dateOpened, dateServer) {
		this.span = new Date().getTime() - dateServer;
		this.dateOpened = dateOpened;
		this.timeOpenedHtml = timeOpenedHtml;
		Code.Clock.isRedHot = false;
		setInterval(Code.Clock.showElapsed, 1000);
	},
	
	initTimeLimited : function(timeLeftHtml, dateEnd, serverTime, redirectUrl) {
		this.dateEnd = dateEnd;
		this.timeLeftHtml = timeLeftHtml;
		this.redirectUrl = redirectUrl;
		setInterval(Code.Clock.showTimeLeft, 1000);
	},
	
	showElapsed : function() {
		Code.Clock.timeOpenedHtml.html(Code.Utils.Helpers.getSpanString(Code.Clock.dateOpened, new Date(), Code.Clock.span));
	},
	
	showTimeLeft : function() {
		var t = Code.Utils.Helpers.getSpan(new Date(), Code.Clock.dateEnd, Code.Clock.span);
		t = Math.floor(t / 1000);
		if(t < 5 * 60 && !Code.Clock.isRedHot) {
			Code.Clock.timeLeftHtml.css("font-weight", "bold");
			Code.Clock.timeLeftHtml.css("color", "#ff0000");
			Code.Clock.isRedHot = true;
		}
		if(t <= 0) {
			window.location.href = Code.Clock.redirectUrl;
		}
		Code.Clock.timeLeftHtml.html(Code.Utils.Helpers.getSpanString(new Date(), Code.Clock.dateEnd, Code.Clock.span))
	}
	
};
