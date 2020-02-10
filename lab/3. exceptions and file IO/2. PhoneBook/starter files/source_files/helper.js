Code.Utils.Helpers = {
  toCanonicalMonth: function( month ) {
    return month < 10 ? "0" + month : month;
  },
  getSpan : function(from, to, span) {
	 return to.getTime() - from.getTime() + span;
  },
  getSpanWarning : function(from, to, span) {
	  var diff = this.getSpan(from, to, span);
	  diff = Math.floor(diff / 1000);
	  var seconds = diff % 60;
	  return seconds < 60 * 5;
  },
  getSpanString : function(from, to, span) {
		var diff = this.getSpan(from, to, span);
		diff = Math.floor(diff / 1000);
		var seconds = diff % 60;
		diff = Math.floor(diff / 60);
		var minutes = diff % 60;
		diff = Math.floor(diff / 60);
		var hours = diff % 24;
		diff = Math.floor(diff / 24);
		var days = diff;
		if(days > 0) {
			if(days > 1) {
				return days + " дена";
			} else {
				return days + " ден";
			}
		} else {
			return ( hours < 10 ? "0" : "" ) + hours + ":" + ( minutes < 10 ? "0" : "" ) + minutes + ":" + ( seconds < 10 ? "0" : "" ) + seconds; 
		}
	}
};