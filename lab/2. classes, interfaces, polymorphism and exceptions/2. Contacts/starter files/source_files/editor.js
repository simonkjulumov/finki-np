Code.Editor = {
	codeEditor : {},
	editors : {},
	markers: [],
	init : function(textArea, mode, isReadOnly) {
 		this.codeEditor = CodeMirror.fromTextArea(textArea, {
		    matchBrackets: true,
		    mode: mode,
		    lineNumbers : true,
		    autoCloseBrackets : true,
		    tabMode : "spaces",
		    indentUnit : 4,
		    readOnly : isReadOnly,
		    gutters: ["CodeMirror-compiler-message"],
			extraKeys: {
		        "F11": function(cm) {
		          cm.setOption("fullScreen", !cm.getOption("fullScreen"));
		        },
		        "Esc": function(cm) {
		          if (cm.getOption("fullScreen")) cm.setOption("fullScreen", false);
		        }
		     }
		});
		this.codeEditor.setSize(null, 520);
		this.codeEditor.onCursorActivity = function() {
			this.codeEditor.matchHighlight("CodeMirror-matchhighlight");
		}

	},
	initInstance : function(key, textArea, mode, isReadOnly) {
		var editor = CodeMirror.fromTextArea(textArea, {
		    matchBrackets: true,
		    mode: mode,
		    lineNumbers : true,
		    tabMode : "spaces",
		    indentUnit : 4,
		    readOnly : isReadOnly,
		    autoCloseBrackets : true,
			extraKeys: {
		        "F11": function(cm) {
		          cm.setOption("fullScreen", !cm.getOption("fullScreen"));
		        },
		        "Esc": function(cm) {
		          if (cm.getOption("fullScreen")) cm.setOption("fullScreen", false);
		        }
		      }
		});
		editor.onCursorActivity = function() {
			editor.matchHighlight("CodeMirror-matchhighlight");
		}
		this.editors[key] = editor;
		
	},
	setTheme : function(theme) {
		this.codeEditor.setOption("theme", theme);
	},
	setMode : function(mode) {
		this.codeEditor.setOption("mode", mode);
	},
	getCode : function() {
		return this.codeEditor.getValue().replace(/\s+&&\s+/, "&&");
	},
	setCode : function(code) {
		this.codeEditor.setValue(code);
	},
	setFixedSize: function() {
		this.codeEditor.setSize(null, 520);
	},
	setAutoSize: function() {
		this.codeEditor.setSize(null, "auto");
	},
	format : function(problemDataId) {
		var code = Code.Editor.getCode();
		$.post('/code/format', { 
	        code : code,
	        	'problemData.id' : problemDataId
	   		},
	   		function(data) {
	   			Code.Editor.setCode(data);
	   		}
	  	);
	},
	analyze : function(problemDataId, callback) {
		var code = Code.Editor.getCode();
		$.post('/code/analyze', { 
	        code : code,
	        	'problemData.id' : problemDataId
	   		},
	   		function(data) {
	   			callback(data);
	   		}
	  	);
	},
	markErrors : function(errors) {
		this.codeEditor.clearGutter("CodeMirror-compiler-message");
		for(var i = 0; i < this.markers.length; ++i) {
			this.markers[i].clear();
		}
		this.markers = [];
		if(errors == null) return;
		var groupedByLine = {};
		function getMaxSeverity(a, b) {
		    if (a == "error") return a;
		    else return b;
		}
		for(var i = 0; i < errors.length; ++i) {
			var error = errors[i];
			var gbl = groupedByLine[error.lineNumber];
			if(gbl) {
				if(error.severity == "error") {
					gbl.severity = error.severity;
				}
				gbl.message += "<br />";
				gbl.message += error.message;
				if(error.columnNumber) {
					gbl.columns.push(error.columnNumber);
				}
			} else {
				gbl = {};
				gbl.lineNumber = error.lineNumber;
				gbl.severity = error.severity;
				gbl.message = error.message;
				if(error.columnNumber) {
					gbl.columns = [error.columnNumber];
				} else {
					gbl.columns = [];
				}
			}
			groupedByLine[error.lineNumber] = gbl;
		}
		for(var lineNumber in groupedByLine) {
			var error = groupedByLine[lineNumber];
			var div = document.createElement("div");
			var icon = document.createElement("i");
			if(error.severity == "error") {
				icon.className = "fa fa-exclamation-circle pull-right";
			} else if(error.severity == "note") {
				icon.className = "fa fa-warning pull-right";
			} else {
				icon.className = "fa fa-info-circle pull-right";
			}
			icon.setAttribute("data-toggle", "tooltip");
			icon.title = error.message;
			$(icon).tooltip({
				'html': true,
				'placement' : 'right'
			});
			div.appendChild(icon);
			this.codeEditor.setGutterMarker(error.lineNumber - 1, "CodeMirror-compiler-message", div);
			for(var i = 0; i < error.columns.length; ++i) {
				var marker = this.codeEditor.markText({line: error.lineNumber - 1, ch: error.columns[i] - 1 }, 
						{line: error.lineNumber - 1, ch: error.columns[i]}, {
					className: "CodeMirror-compiler-message-" + error.severity
				});
				this.markers.push(marker);
			}
		}
	},
	clearErrors : function() {
		this.codeEditor.clearGutter("CodeMirror-compiler-message");
	}
};
