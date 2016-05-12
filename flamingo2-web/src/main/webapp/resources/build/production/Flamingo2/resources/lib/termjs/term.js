(function () {
    var i = this, p = this.document;

    function h() {
        this._events = this._events || {}
    }

    h.prototype.addListener = function (y, z) {
        this._events[y] = this._events[y] || [];
        this._events[y].push(z)
    };
    h.prototype.on = h.prototype.addListener;
    h.prototype.removeListener = function (z, A) {
        if (!this._events[z]) {
            return
        }
        var B = this._events[z], y = B.length;
        while (y--) {
            if (B[y] === A || B[y].listener === A) {
                B.splice(y, 1);
                return
            }
        }
    };
    h.prototype.off = h.prototype.removeListener;
    h.prototype.removeAllListeners = function (y) {
        if (this._events[y]) {
            delete this._events[y]
        }
    };
    h.prototype.once = function (z, A) {
        function y() {
            var B = Array.prototype.slice.call(arguments);
            this.removeListener(z, y);
            return A.apply(this, B)
        }

        y.listener = A;
        return this.on(z, y)
    };
    h.prototype.emit = function (B) {
        if (!this._events[B]) {
            return
        }
        var z = Array.prototype.slice.call(arguments, 1), C = this._events[B], y = C.length, A = 0;
        for (; A < y; A++) {
            C[A].apply(this, z)
        }
    };
    h.prototype.listeners = function (y) {
        return this._events[y] = this._events[y] || []
    };
    var x = 0, g = 1, q = 2, j = 3, f = 4, o = 5, s = 6;

    function t(z) {
        var y = this;
        if (!(this instanceof t)) {
            return new t(arguments[0], arguments[1], arguments[2])
        }
        h.call(this);
        if (typeof z === "number") {
            z = {cols: arguments[0], rows: arguments[1], handler: arguments[2]}
        }
        z = z || {};
        e(n(t.defaults), function (B) {
            if (z[B] == null) {
                z[B] = t.options[B];
                if (t[B] !== t.defaults[B]) {
                    z[B] = t[B]
                }
            }
            y[B] = z[B]
        });
        if (z.colors.length === 8) {
            z.colors = z.colors.concat(t._colors.slice(8))
        } else {
            if (z.colors.length === 16) {
                z.colors = z.colors.concat(t._colors.slice(16))
            } else {
                if (z.colors.length === 10) {
                    z.colors = z.colors.slice(0, -2).concat(t._colors.slice(8, -2), z.colors.slice(-2))
                } else {
                    if (z.colors.length === 18) {
                        z.colors = z.colors.slice(0, -2).concat(t._colors.slice(16, -2), z.colors.slice(-2))
                    }
                }
            }
        }
        this.font_family = z.font_family || '"Monaco", "Gulimche", "Courier New", "DejaVu Sans Mono", "Liberation Mono", monospace';
        this.font_size = z.font_size || 12;
        this.colors = z.colors;
        this.options = z;
        this.parent = z.body || z.parent || (p ? p.getElementsByTagName("body")[0] : null);
        this.cols = z.cols || z.geometry[0];
        this.rows = z.rows || z.geometry[1];
        if (z.handler) {
            this.on("data", z.handler)
        }
        this.ybase = 0;
        this.ydisp = 0;
        this.x = 0;
        this.y = 0;
        this.cursorState = 0;
        this.cursorHidden = false;
        this.convertEol;
        this.state = 0;
        this.queue = "";
        this.scrollTop = 0;
        this.scrollBottom = this.rows - 1;
        this.applicationKeypad = false;
        this.applicationCursor = false;
        this.originMode = false;
        this.insertMode = false;
        this.wraparoundMode = false;
        this.normal = null;
        this.prefixMode = false;
        this.selectMode = false;
        this.visualMode = false;
        this.searchMode = false;
        this.searchDown;
        this.entry = "";
        this.entryPrefix = "Search: ";
        this._real;
        this._selected;
        this._textarea;
        this.charset = null;
        this.gcharset = null;
        this.glevel = 0;
        this.charsets = [null];
        this.decLocator;
        this.x10Mouse;
        this.vt200Mouse;
        this.vt300Mouse;
        this.normalMouse;
        this.mouseEvents;
        this.sendFocus;
        this.utfMouse;
        this.sgrMouse;
        this.urxvtMouse;
        this.element;
        this.children;
        this.refreshStart;
        this.refreshEnd;
        this.savedX;
        this.savedY;
        this.savedCols;
        this.readable = true;
        this.writable = true;
        this.defAttr = (0 << 18) | (257 << 9) | (256 << 0);
        this.curAttr = this.defAttr;
        this.params = [];
        this.currentParam = 0;
        this.prefix = "";
        this.postfix = "";
        this.lines = [];
        var A = this.rows;
        while (A--) {
            this.lines.push(this.blankLine())
        }
        this.tabs;
        this.setupStops()
    }

    l(t, h);
    t.prototype.eraseAttr = function () {
        return (this.defAttr & ~511) | (this.curAttr & 511)
    };
    t.tangoColors = ["#2e3436", "#cc0000", "#4e9a06", "#c4a000", "#3465a4", "#75507b", "#06989a", "#d3d7cf", "#555753", "#ef2929", "#8ae234", "#fce94f", "#729fcf", "#ad7fa8", "#34e2e2", "#eeeeec"];
    t.xtermColors = ["#000000", "#cd0000", "#00cd00", "#cdcd00", "#0000ee", "#cd00cd", "#00cdcd", "#e5e5e5", "#7f7f7f", "#ff0000", "#00ff00", "#ffff00", "#5c5cff", "#ff00ff", "#00ffff", "#ffffff"];
    t.colors = (function () {
        var y = t.tangoColors.slice(), C = [0, 95, 135, 175, 215, 255], A;
        A = 0;
        for (; A < 216; A++) {
            z(C[(A / 36) % 6 | 0], C[(A / 6) % 6 | 0], C[A % 6])
        }
        A = 0;
        for (; A < 24; A++) {
            C = 8 + A * 10;
            z(C, C, C)
        }
        function z(F, E, D) {
            y.push("#" + B(F) + B(E) + B(D))
        }

        function B(D) {
            D = D.toString(16);
            return D.length < 2 ? "0" + D : D
        }

        return y
    })();
    t.colors[256] = "#000000";
    t.colors[257] = "#f0f0f0";
    t._colors = t.colors.slice();
    t.vcolors = (function () {
        var A = [], y = t.colors, B = 0, z;
        for (; B < 256; B++) {
            z = parseInt(y[B].substring(1), 16);
            A.push([(z >> 16) & 255, (z >> 8) & 255, z & 255])
        }
        return A
    })();
    t.defaults = {
        colors: t.colors,
        convertEol: false,
        termName: "xterm",
        geometry: [80, 24],
        cursorBlink: true,
        visualBell: false,
        popOnBell: false,
        scrollback: 1000,
        screenKeys: false,
        debug: false,
        useStyle: false,
        font_family : '"Monaco", "Gulimche", "Courier New", "DejaVu Sans Mono", "Liberation Mono", monospace',
        font_size : 12
    };
    t.options = {};
    e(n(t.defaults), function (y) {
        t[y] = t.defaults[y];
        t.options[y] = t.defaults[y]
    });
    t.focus = null;
    t.prototype.focus = function () {
        if (t.focus === this) {
            return
        }
        if (t.focus) {
            t.focus.blur()
        }
        if (this.sendFocus) {
            this.send("\x1b[I")
        }
        this.showCursor();
        t.focus = this
    };
    t.prototype.blur = function () {
        if (t.focus !== this) {
            return
        }
        this.cursorState = 0;
        this.refresh(this.y, this.y);
        if (this.sendFocus) {
            this.send("\x1b[O")
        }
        t.focus = null
    };
    t.prototype.initGlobal = function () {
        var y = this.document;
        t._boundDocs = t._boundDocs || [];
        if (~c(t._boundDocs, y)) {
            return
        }
        t._boundDocs.push(y);
        t.bindPaste(y);
        t.bindKeys(y);
        t.bindCopy(y);
        if (this.isMobile) {
            this.fixMobile(y)
        }
        if (this.useStyle) {
            t.insertStyle(y, this.colors[256], this.colors[257])
        }
    };
    t.bindPaste = function (y) {
        var z = y.defaultView;
        k(z, "paste", function (B) {
            var A = t.focus;
            if (!A) {
                return
            }
            if (B.clipboardData) {
                A.send(B.clipboardData.getData("text/plain"))
            } else {
                if (A.context.clipboardData) {
                    A.send(A.context.clipboardData.getData("Text"))
                }
            }
            A.element.contentEditable = "inherit";
            return d(B)
        })
    };
    t.bindKeys = function (y) {
        k(y, "keydown", function (z) {
            if (!t.focus) {
                return
            }
            var A = z.target || z.srcElement;
            if (!A) {
                return
            }
            if (A === t.focus.element || A === t.focus.context || A === t.focus.document || A === t.focus.body || A === t._textarea || A === t.focus.parent) {
                return t.focus.keyDown(z)
            }
        }, true);
        k(y, "keypress", function (z) {
            if (!t.focus) {
                return
            }
            var A = z.target || z.srcElement;
            if (!A) {
                return
            }
            if (A === t.focus.element || A === t.focus.context || A === t.focus.document || A === t.focus.body || A === t._textarea || A === t.focus.parent) {
                return t.focus.keyPress(z)
            }
        }, true);
        k(y, "mousedown", function (A) {
            if (!t.focus) {
                return
            }
            var z = A.target || A.srcElement;
            if (!z) {
                return
            }
            do {
                if (z === t.focus.element) {
                    return
                }
            } while (z = z.parentNode);
            t.focus.blur()
        })
    };
    t.bindCopy = function (y) {
        var z = y.defaultView;
        k(z, "copy", function (C) {
            var B = t.focus;
            if (!B) {
                return
            }
            if (!B._selected) {
                return
            }
            var A = B.getCopyTextarea();
            var D = B.grabText(B._selected.x1, B._selected.x2, B._selected.y1, B._selected.y2);
            B.emit("copy", D);
            A.focus();
            A.textContent = D;
            A.value = D;
            A.setSelectionRange(0, D.length);
            b(function () {
                B.element.focus();
                B.focus()
            }, 1)
        })
    };
    t.prototype.fixMobile = function (y) {
        var A = this;
        var z = y.createElement("textarea");
        z.style.position = "absolute";
        z.style.left = "-32000px";
        z.style.top = "-32000px";
        z.style.width = "0px";
        z.style.height = "0px";
        z.style.opacity = "0";
        z.style.backgroundColor = "transparent";
        z.style.borderStyle = "none";
        z.style.outlineStyle = "none";
        z.autocapitalize = "none";
        z.autocorrect = "off";
        y.getElementsByTagName("body")[0].appendChild(z);
        t._textarea = z;
        b(function () {
            z.focus()
        }, 1000);
        if (this.isAndroid) {
            k(z, "change", function () {
                var B = z.textContent || z.value;
                z.value = "";
                z.textContent = "";
                A.send(B + "\r")
            })
        }
    };
    t.insertStyle = function (y, B, z) {
        var C = y.getElementById("term-style");
        if (C) {
            return
        }
        var A = y.getElementsByTagName("head")[0];
        if (!A) {
            return
        }
        var C = y.createElement("style");
        C.id = "term-style";
        C.innerHTML = ".terminal {\n  " +
        "float: left;\n  " +
        "border: " + B + ' solid 5px;\n ' +
        'white-space:nowrap;\n ' +
        'font-family: '+t.font_family+';\n  ' +
        'font-size: '+t.font_size+'px;\n  ' +
        'color: ' + z + ";\n  " +
        "background: " + B + ";\n" +
        "}\n\n" +
        ".terminal-cursor {\n  " +
        "" +
        "color: " + B + ";\n  " +
        "background: " + z + ";\n" +
        "}\n";
        A.insertBefore(C, A.firstChild)
    };
    t.prototype.open = function (A) {
        var y = this, z = 0, B;
        this.parent = A || this.parent;
        if (!this.parent) {
            throw new Error("Terminal requires a parent element.")
        }
        this.context = this.parent.ownerDocument.defaultView;
        this.document = this.parent.ownerDocument;
        this.body = this.document.getElementsByTagName("body")[0];
        if (this.context.navigator && this.context.navigator.userAgent) {
            this.isMac = !!~this.context.navigator.userAgent.indexOf("Mac");
            this.isIpad = !!~this.context.navigator.userAgent.indexOf("iPad");
            this.isIphone = !!~this.context.navigator.userAgent.indexOf("iPhone");
            this.isAndroid = !!~this.context.navigator.userAgent.indexOf("Android");
            this.isMobile = this.isIpad || this.isIphone || this.isAndroid;
            this.isMSIE = !!~this.context.navigator.userAgent.indexOf("MSIE")
        }
        this.element = this.document.createElement("div");
        this.element.className = "terminal";
        this.element.style.outline = "none";
        this.element.setAttribute("tabindex", 0);
        this.element.setAttribute("spellcheck", "false");
        this.element.style.backgroundColor = this.colors[256];
        this.element.style.color = this.colors[257];
        this.children = [];
        for (; z < this.rows; z++) {
            B = this.document.createElement("div");
            this.element.appendChild(B);
            this.children.push(B)
        }
        this.parent.appendChild(this.element);
        this.refresh(0, this.rows - 1);
        this.initGlobal();
        this.focus();
        this.startBlink();
        k(this.element, "focus", function () {
            y.focus();
            if (y.isMobile) {
                t._textarea.focus()
            }
        });
        k(this.element, "mousedown", function () {
            y.focus()
        });
        k(this.element, "mousedown", function (D) {
            var C = D.button != null ? +D.button : D.which != null ? D.which - 1 : null;
            if (y.isMSIE) {
                C = C === 1 ? 0 : C === 4 ? 1 : C
            }
            if (C !== 2) {
                return
            }
            y.element.contentEditable = "true";
            b(function () {
                y.element.contentEditable = "inherit"
            }, 1)
        }, true);
        this.bindMouse();
        if (t.brokenBold == null) {
            t.brokenBold = v(this.document)
        }
        b(function () {
            y.element.focus()
        }, 100)
    };
    t.prototype.bindMouse = function () {
        var z = this.element, H = this, B = 32;
        var C = "onmousewheel" in this.context ? "mousewheel" : "DOMMouseScroll";

        function F(J) {
            var I, K;
            I = y(J);
            K = A(J);
            if (!K) {
                return
            }
            G(I, K);
            switch (J.type) {
                case"mousedown":
                    B = I;
                    break;
                case"mouseup":
                    B = 32;
                    break;
                case C:
                    break
            }
        }

        function E(J) {
            var I = B, K;
            K = A(J);
            if (!K) {
                return
            }
            I += 32;
            G(I, K)
        }

        function D(J, I) {
            if (!H.utfMouse) {
                if (I === 255) {
                    return J.push(0)
                }
                if (I > 127) {
                    I = 127
                }
                J.push(I)
            } else {
                if (I === 2047) {
                    return J.push(0)
                }
                if (I < 127) {
                    J.push(I)
                } else {
                    if (I > 2047) {
                        I = 2047
                    }
                    J.push(192 | (I >> 6));
                    J.push(128 | (I & 63))
                }
            }
        }

        function G(I, K) {
            if (H.vt300Mouse) {
                I &= 3;
                K.x -= 32;
                K.y -= 32;
                var J = "\x1b[24";
                if (I === 0) {
                    J += "1"
                } else {
                    if (I === 1) {
                        J += "3"
                    } else {
                        if (I === 2) {
                            J += "5"
                        } else {
                            if (I === 3) {
                                return
                            } else {
                                J += "0"
                            }
                        }
                    }
                }
                J += "~[" + K.x + "," + K.y + "]\r";
                H.send(J);
                return
            }
            if (H.decLocator) {
                I &= 3;
                K.x -= 32;
                K.y -= 32;
                if (I === 0) {
                    I = 2
                } else {
                    if (I === 1) {
                        I = 4
                    } else {
                        if (I === 2) {
                            I = 6
                        } else {
                            if (I === 3) {
                                I = 3
                            }
                        }
                    }
                }
                H.send("\x1b[" + I + ";" + (I === 3 ? 4 : 0) + ";" + K.y + ";" + K.x + ";" + (K.page || 0) + "&w");
                return
            }
            if (H.urxvtMouse) {
                K.x -= 32;
                K.y -= 32;
                K.x++;
                K.y++;
                H.send("\x1b[" + I + ";" + K.x + ";" + K.y + "M");
                return
            }
            if (H.sgrMouse) {
                K.x -= 32;
                K.y -= 32;
                H.send("\x1b[<" + ((I & 3) === 3 ? I & ~3 : I) + ";" + K.x + ";" + K.y + ((I & 3) === 3 ? "m" : "M"));
                return
            }
            var J = [];
            D(J, I);
            D(J, K.x);
            D(J, K.y);
            H.send("\x1b[M" + u.fromCharCode.apply(u, J))
        }

        function y(L) {
            var K, I, N, M, J;
            switch (L.type) {
                case"mousedown":
                    K = L.button != null ? +L.button : L.which != null ? L.which - 1 : null;
                    if (H.isMSIE) {
                        K = K === 1 ? 0 : K === 4 ? 1 : K
                    }
                    break;
                case"mouseup":
                    K = 3;
                    break;
                case"DOMMouseScroll":
                    K = L.detail < 0 ? 64 : 65;
                    break;
                case"mousewheel":
                    K = L.wheelDeltaY > 0 ? 64 : 65;
                    break
            }
            I = L.shiftKey ? 4 : 0;
            N = L.metaKey ? 8 : 0;
            M = L.ctrlKey ? 16 : 0;
            J = I | N | M;
            if (H.vt200Mouse) {
                J &= M
            } else {
                if (!H.normalMouse) {
                    J = 0
                }
            }
            K = (32 + (J << 2)) + K;
            return K
        }

        function A(M) {
            var I, N, J, L, K;
            if (M.pageX == null) {
                return
            }
            I = M.pageX;
            N = M.pageY;
            K = H.element;
            while (K && K !== H.document.documentElement) {
                I -= K.offsetLeft;
                N -= K.offsetTop;
                K = "offsetParent" in K ? K.offsetParent : K.parentNode
            }
            J = H.element.clientWidth;
            L = H.element.clientHeight;
            I = Math.round((I / J) * H.cols);
            N = Math.round((N / L) * H.rows);
            if (I < 0) {
                I = 0
            }
            if (I > H.cols) {
                I = H.cols
            }
            if (N < 0) {
                N = 0
            }
            if (N > H.rows) {
                N = H.rows
            }
            I += 32;
            N += 32;
            return {x: I, y: N, type: M.type === C ? "mousewheel" : M.type}
        }

        k(z, "mousedown", function (J) {
            if (!H.mouseEvents) {
                return
            }
            F(J);
            H.focus();
            if (H.vt200Mouse) {
                F({__proto__: J, type: "mouseup"});
                return d(J)
            }
            if (H.normalMouse) {
                k(H.document, "mousemove", E)
            }
            if (!H.x10Mouse) {
                k(H.document, "mouseup", function I(K) {
                    F(K);
                    if (H.normalMouse) {
                        w(H.document, "mousemove", E)
                    }
                    w(H.document, "mouseup", I);
                    return d(K)
                })
            }
            return d(J)
        });
        k(z, C, function (I) {
            if (!H.mouseEvents) {
                return
            }
            if (H.x10Mouse || H.vt300Mouse || H.decLocator) {
                return
            }
            F(I);
            return d(I)
        });
        k(z, C, function (I) {
            if (H.mouseEvents) {
                return
            }
            if (H.applicationKeypad) {
                return
            }
            if (I.type === "DOMMouseScroll") {
                H.scrollDisp(I.detail < 0 ? -5 : 5)
            } else {
                H.scrollDisp(I.wheelDeltaY > 0 ? -5 : 5)
            }
            return d(I)
        })
    };
    t.prototype.destroy = function () {
        this.readable = false;
        this.writable = false;
        this._events = {};
        this.handler = function () {
        };
        this.write = function () {
        };
        if (this.element.parentNode) {
            this.element.parentNode.removeChild(this.element)
        }
    };
    t.prototype.refresh = function (C, E) {
        var L, K, I, O, F, A, B, H, J, G, z, D, N, M;
        if (E - C >= this.rows / 2) {
            M = this.element.parentNode;
            if (M) {
                M.removeChild(this.element)
            }
        }
        B = this.cols;
        K = C;
        if (E >= this.lines.length) {
            this.log("`end` is too large. Most likely a bad CSR.");
            E = this.lines.length - 1
        }
        for (; K <= E; K++) {
            N = K + this.ydisp;
            O = this.lines[N];
            F = "";
            if (K === this.y && this.cursorState && (this.ydisp === this.ybase || this.selectMode) && !this.cursorHidden) {
                L = this.x
            } else {
                L = -1
            }
            J = this.defAttr;
            I = 0;
            for (; I < B; I++) {
                H = O[I][0];
                A = O[I][1];
                if (I === L) {
                    H = -1
                }
                if (H !== J) {
                    if (J !== this.defAttr) {
                        F += "</span>"
                    }
                    if (H !== this.defAttr) {
                        if (H === -1) {
                            F += '<span class="reverse-video terminal-cursor">'
                        } else {
                            F += '<span style="';
                            G = H & 511;
                            z = (H >> 9) & 511;
                            D = H >> 18;
                            if (D & 1) {
                                if (!t.brokenBold) {
                                    F += "font-weight:bold;"
                                }
                                if (z < 8) {
                                    z += 8
                                }
                            }
                            if (D & 2) {
                                F += "text-decoration:underline;"
                            }
                            if (D & 4) {
                                if (D & 2) {
                                    F = F.slice(0, -1);
                                    F += " blink;"
                                } else {
                                    F += "text-decoration:blink;"
                                }
                            }
                            if (D & 8) {
                                G = (H >> 9) & 511;
                                z = H & 511;
                                if ((D & 1) && z < 8) {
                                    z += 8
                                }
                            }
                            if (D & 16) {
                                F += "visibility:hidden;"
                            }
                            if (G !== 256) {
                                F += "background-color:" + this.colors[G] + ";"
                            }
                            if (z !== 257) {
                                F += "color:" + this.colors[z] + ";"
                            }
                            F += '">'
                        }
                    }
                }
                switch (A) {
                    case"&":
                        F += "&amp;";
                        break;
                    case"<":
                        F += "&lt;";
                        break;
                    case">":
                        F += "&gt;";
                        break;
                    default:
                        if (A <= " ") {
                            F += "&nbsp;"
                        } else {
                            if (r(A)) {
                                I++
                            }
                            F += A
                        }
                        break
                }
                J = H
            }
            if (J !== this.defAttr) {
                F += "</span>"
            }
            this.children[K].innerHTML = F
        }
        if (M) {
            M.appendChild(this.element)
        }
    };
    t.prototype._cursorBlink = function () {
        if (t.focus !== this) {
            return
        }
        this.cursorState ^= 1;
        this.refresh(this.y, this.y)
    };
    t.prototype.showCursor = function () {
        if (!this.cursorState) {
            this.cursorState = 1;
            this.refresh(this.y, this.y)
        } else {
        }
    };
    t.prototype.startBlink = function () {
        if (!this.cursorBlink) {
            return
        }
        var y = this;
        this._blinker = function () {
            y._cursorBlink()
        };
        this._blink = a(this._blinker, 500)
    };
    t.prototype.refreshBlink = function () {
        if (!this.cursorBlink) {
            return
        }
        clearInterval(this._blink);
        this._blink = a(this._blinker, 500)
    };
    t.prototype.scroll = function () {
        var y;
        if (++this.ybase === this.scrollback) {
            this.ybase = this.ybase / 2 | 0;
            this.lines = this.lines.slice(-(this.ybase + this.rows) + 1)
        }
        this.ydisp = this.ybase;
        y = this.ybase + this.rows - 1;
        y -= this.rows - 1 - this.scrollBottom;
        if (y === this.lines.length) {
            this.lines.push(this.blankLine())
        } else {
            this.lines.splice(y, 0, this.blankLine())
        }
        if (this.scrollTop !== 0) {
            if (this.ybase !== 0) {
                this.ybase--;
                this.ydisp = this.ybase
            }
            this.lines.splice(this.ybase + this.scrollTop, 1)
        }
        this.updateRange(this.scrollTop);
        this.updateRange(this.scrollBottom)
    };
    t.prototype.scrollDisp = function (y) {
        this.ydisp += y;
        if (this.ydisp > this.ybase) {
            this.ydisp = this.ybase
        } else {
            if (this.ydisp < 0) {
                this.ydisp = 0
            }
        }
        this.refresh(0, this.rows - 1)
    };
    t.prototype.write = function (E) {
        var y = E.length, A = 0, z, C, B;
        this.refreshStart = this.y;
        this.refreshEnd = this.y;
        if (this.ybase !== this.ydisp) {
            this.ydisp = this.ybase;
            this.maxRange()
        }
        for (; A < y; A++) {
            B = E[A];
            switch (this.state) {
                case x:
                    switch (B) {
                        case"\x07":
                            this.bell();
                            break;
                        case"\n":
                        case"\x0b":
                        case"\x0c":
                            if (this.convertEol) {
                                this.x = 0
                            }
                            this.y++;
                            if (this.y > this.scrollBottom) {
                                this.y--;
                                this.scroll()
                            }
                            break;
                        case"\r":
                            this.x = 0;
                            break;
                        case"\x08":
                            if (this.x > 0) {
                                this.x--
                            }
                            break;
                        case"\t":
                            this.x = this.nextStop();
                            break;
                        case"\x0e":
                            this.setgLevel(1);
                            break;
                        case"\x0f":
                            this.setgLevel(0);
                            break;
                        case"\x1b":
                            this.state = g;
                            break;
                        default:
                            if (B >= " ") {
                                if (this.charset && this.charset[B]) {
                                    B = this.charset[B]
                                }
                                if (this.x >= this.cols) {
                                    this.x = 0;
                                    this.y++;
                                    if (this.y > this.scrollBottom) {
                                        this.y--;
                                        this.scroll()
                                    }
                                }
                                this.lines[this.y + this.ybase][this.x] = [this.curAttr, B];
                                this.x++;
                                this.updateRange(this.y);
                                if (r(B)) {
                                    z = this.y + this.ybase;
                                    if (this.cols < 2 || this.x >= this.cols) {
                                        this.lines[z][this.x - 1] = [this.curAttr, " "];
                                        break
                                    }
                                    this.lines[z][this.x] = [this.curAttr, " "];
                                    this.x++
                                }
                            }
                            break
                    }
                    break;
                case g:
                    switch (B) {
                        case"[":
                            this.params = [];
                            this.currentParam = 0;
                            this.state = q;
                            break;
                        case"]":
                            this.params = [];
                            this.currentParam = 0;
                            this.state = j;
                            break;
                        case"P":
                            this.params = [];
                            this.currentParam = 0;
                            this.state = o;
                            break;
                        case"_":
                            this.state = s;
                            break;
                        case"^":
                            this.state = s;
                            break;
                        case"c":
                            this.reset();
                            break;
                        case"E":
                            this.x = 0;
                        case"D":
                            this.index();
                            break;
                        case"M":
                            this.reverseIndex();
                            break;
                        case"%":
                            this.setgLevel(0);
                            this.setgCharset(0, t.charsets.US);
                            this.state = x;
                            A++;
                            break;
                        case"(":
                        case")":
                        case"*":
                        case"+":
                        case"-":
                        case".":
                            switch (B) {
                                case"(":
                                    this.gcharset = 0;
                                    break;
                                case")":
                                    this.gcharset = 1;
                                    break;
                                case"*":
                                    this.gcharset = 2;
                                    break;
                                case"+":
                                    this.gcharset = 3;
                                    break;
                                case"-":
                                    this.gcharset = 1;
                                    break;
                                case".":
                                    this.gcharset = 2;
                                    break
                            }
                            this.state = f;
                            break;
                        case"/":
                            this.gcharset = 3;
                            this.state = f;
                            A--;
                            break;
                        case"N":
                            break;
                        case"O":
                            break;
                        case"n":
                            this.setgLevel(2);
                            break;
                        case"o":
                            this.setgLevel(3);
                            break;
                        case"|":
                            this.setgLevel(3);
                            break;
                        case"}":
                            this.setgLevel(2);
                            break;
                        case"~":
                            this.setgLevel(1);
                            break;
                        case"7":
                            this.saveCursor();
                            this.state = x;
                            break;
                        case"8":
                            this.restoreCursor();
                            this.state = x;
                            break;
                        case"#":
                            this.state = x;
                            A++;
                            break;
                        case"H":
                            this.tabSet();
                            break;
                        case"=":
                            this.log("Serial port requested application keypad.");
                            this.applicationKeypad = true;
                            this.state = x;
                            break;
                        case">":
                            this.log("Switching back to normal keypad.");
                            this.applicationKeypad = false;
                            this.state = x;
                            break;
                        default:
                            this.state = x;
                            this.error("Unknown ESC control: %s.", B);
                            break
                    }
                    break;
                case f:
                    switch (B) {
                        case"0":
                            C = t.charsets.SCLD;
                            break;
                        case"A":
                            C = t.charsets.UK;
                            break;
                        case"B":
                            C = t.charsets.US;
                            break;
                        case"4":
                            C = t.charsets.Dutch;
                            break;
                        case"C":
                        case"5":
                            C = t.charsets.Finnish;
                            break;
                        case"R":
                            C = t.charsets.French;
                            break;
                        case"Q":
                            C = t.charsets.FrenchCanadian;
                            break;
                        case"K":
                            C = t.charsets.German;
                            break;
                        case"Y":
                            C = t.charsets.Italian;
                            break;
                        case"E":
                        case"6":
                            C = t.charsets.NorwegianDanish;
                            break;
                        case"Z":
                            C = t.charsets.Spanish;
                            break;
                        case"H":
                        case"7":
                            C = t.charsets.Swedish;
                            break;
                        case"=":
                            C = t.charsets.Swiss;
                            break;
                        case"/":
                            C = t.charsets.ISOLatin;
                            A++;
                            break;
                        default:
                            C = t.charsets.US;
                            break
                    }
                    this.setgCharset(this.gcharset, C);
                    this.gcharset = null;
                    this.state = x;
                    break;
                case j:
                    if (B === "\x1b" || B === "\x07") {
                        if (B === "\x1b") {
                            A++
                        }
                        this.params.push(this.currentParam);
                        switch (this.params[0]) {
                            case 0:
                            case 1:
                            case 2:
                                if (this.params[1]) {
                                    this.title = this.params[1];
                                    this.handleTitle(this.title)
                                }
                                break;
                            case 3:
                                break;
                            case 4:
                            case 5:
                                break;
                            case 10:
                            case 11:
                            case 12:
                            case 13:
                            case 14:
                            case 15:
                            case 16:
                            case 17:
                            case 18:
                            case 19:
                                break;
                            case 46:
                                break;
                            case 50:
                                break;
                            case 51:
                                break;
                            case 52:
                                break;
                            case 104:
                            case 105:
                            case 110:
                            case 111:
                            case 112:
                            case 113:
                            case 114:
                            case 115:
                            case 116:
                            case 117:
                            case 118:
                                break
                        }
                        this.params = [];
                        this.currentParam = 0;
                        this.state = x
                    } else {
                        if (!this.params.length) {
                            if (B >= "0" && B <= "9") {
                                this.currentParam = this.currentParam * 10 + B.charCodeAt(0) - 48
                            } else {
                                if (B === ";") {
                                    this.params.push(this.currentParam);
                                    this.currentParam = ""
                                }
                            }
                        } else {
                            this.currentParam += B
                        }
                    }
                    break;
                case q:
                    if (B === "?" || B === ">" || B === "!") {
                        this.prefix = B;
                        break
                    }
                    if (B >= "0" && B <= "9") {
                        this.currentParam = this.currentParam * 10 + B.charCodeAt(0) - 48;
                        break
                    }
                    if (B === "$" || B === '"' || B === " " || B === "'") {
                        this.postfix = B;
                        break
                    }
                    this.params.push(this.currentParam);
                    this.currentParam = 0;
                    if (B === ";") {
                        break
                    }
                    this.state = x;
                    switch (B) {
                        case"A":
                            this.cursorUp(this.params);
                            break;
                        case"B":
                            this.cursorDown(this.params);
                            break;
                        case"C":
                            this.cursorForward(this.params);
                            break;
                        case"D":
                            this.cursorBackward(this.params);
                            break;
                        case"H":
                            this.cursorPos(this.params);
                            break;
                        case"J":
                            this.eraseInDisplay(this.params);
                            break;
                        case"K":
                            this.eraseInLine(this.params);
                            break;
                        case"m":
                            if (!this.prefix) {
                                this.charAttributes(this.params)
                            }
                            break;
                        case"n":
                            if (!this.prefix) {
                                this.deviceStatus(this.params)
                            }
                            break;
                        case"@":
                            this.insertChars(this.params);
                            break;
                        case"E":
                            this.cursorNextLine(this.params);
                            break;
                        case"F":
                            this.cursorPrecedingLine(this.params);
                            break;
                        case"G":
                            this.cursorCharAbsolute(this.params);
                            break;
                        case"L":
                            this.insertLines(this.params);
                            break;
                        case"M":
                            this.deleteLines(this.params);
                            break;
                        case"P":
                            this.deleteChars(this.params);
                            break;
                        case"X":
                            this.eraseChars(this.params);
                            break;
                        case"`":
                            this.charPosAbsolute(this.params);
                            break;
                        case"a":
                            this.HPositionRelative(this.params);
                            break;
                        case"c":
                            this.sendDeviceAttributes(this.params);
                            break;
                        case"d":
                            this.linePosAbsolute(this.params);
                            break;
                        case"e":
                            this.VPositionRelative(this.params);
                            break;
                        case"f":
                            this.HVPosition(this.params);
                            break;
                        case"h":
                            this.setMode(this.params);
                            break;
                        case"l":
                            this.resetMode(this.params);
                            break;
                        case"r":
                            this.setScrollRegion(this.params);
                            break;
                        case"s":
                            this.saveCursor(this.params);
                            break;
                        case"u":
                            this.restoreCursor(this.params);
                            break;
                        case"I":
                            this.cursorForwardTab(this.params);
                            break;
                        case"S":
                            this.scrollUp(this.params);
                            break;
                        case"T":
                            if (this.params.length < 2 && !this.prefix) {
                                this.scrollDown(this.params)
                            }
                            break;
                        case"Z":
                            this.cursorBackwardTab(this.params);
                            break;
                        case"b":
                            this.repeatPrecedingCharacter(this.params);
                            break;
                        case"g":
                            this.tabClear(this.params);
                            break;
                        case"p":
                            switch (this.prefix) {
                                case"!":
                                    this.softReset(this.params);
                                    break
                            }
                            break;
                        default:
                            this.error("Unknown CSI code: %s.", B);
                            break
                    }
                    this.prefix = "";
                    this.postfix = "";
                    break;
                case o:
                    if (B === "\x1b" || B === "\x07") {
                        if (B === "\x1b") {
                            A++
                        }
                        switch (this.prefix) {
                            case"":
                                break;
                            case"$q":
                                var F = this.currentParam, D = false;
                                switch (F) {
                                    case'"q':
                                        F = '0"q';
                                        break;
                                    case'"p':
                                        F = '61"p';
                                        break;
                                    case"r":
                                        F = "" + (this.scrollTop + 1) + ";" + (this.scrollBottom + 1) + "r";
                                        break;
                                    case"m":
                                        F = "0m";
                                        break;
                                    default:
                                        this.error("Unknown DCS Pt: %s.", F);
                                        F = "";
                                        break
                                }
                                this.send("\x1bP" + +D + "$r" + F + "\x1b\\");
                                break;
                            case"+p":
                                break;
                            case"+q":
                                var F = this.currentParam, D = false;
                                this.send("\x1bP" + +D + "+r" + F + "\x1b\\");
                                break;
                            default:
                                this.error("Unknown DCS prefix: %s.", this.prefix);
                                break
                        }
                        this.currentParam = 0;
                        this.prefix = "";
                        this.state = x
                    } else {
                        if (!this.currentParam) {
                            if (!this.prefix && B !== "$" && B !== "+") {
                                this.currentParam = B
                            } else {
                                if (this.prefix.length === 2) {
                                    this.currentParam = B
                                } else {
                                    this.prefix += B
                                }
                            }
                        } else {
                            this.currentParam += B
                        }
                    }
                    break;
                case s:
                    if (B === "\x1b" || B === "\x07") {
                        if (B === "\x1b") {
                            A++
                        }
                        this.state = x
                    }
                    break
            }
        }
        this.updateRange(this.y);
        this.refresh(this.refreshStart, this.refreshEnd)
    };
    t.prototype.writeln = function (y) {
        this.write(y + "\r\n")
    };
    t.prototype.keyDown = function (A) {
        var y = this, z;
        switch (A.keyCode) {
            case 8:
                if (A.shiftKey) {
                    z = "\x08";
                    break
                }
                z = "\x7f";
                break;
            case 9:
                if (A.shiftKey) {
                    z = "\x1b[Z";
                    break
                }
                z = "\t";
                break;
            case 13:
                z = "\r";
                break;
            case 27:
                z = "\x1b";
                break;
            case 37:
                if (this.applicationCursor) {
                    z = "\x1bOD";
                    break
                }
                z = "\x1b[D";
                break;
            case 39:
                if (this.applicationCursor) {
                    z = "\x1bOC";
                    break
                }
                z = "\x1b[C";
                break;
            case 38:
                if (this.applicationCursor) {
                    z = "\x1bOA";
                    break
                }
                if (A.ctrlKey) {
                    this.scrollDisp(-1);
                    return d(A)
                } else {
                    z = "\x1b[A"
                }
                break;
            case 40:
                if (this.applicationCursor) {
                    z = "\x1bOB";
                    break
                }
                if (A.ctrlKey) {
                    this.scrollDisp(1);
                    return d(A)
                } else {
                    z = "\x1b[B"
                }
                break;
            case 46:
                z = "\x1b[3~";
                break;
            case 45:
                z = "\x1b[2~";
                break;
            case 36:
                if (this.applicationKeypad) {
                    z = "\x1bOH";
                    break
                }
                z = "\x1bOH";
                break;
            case 35:
                if (this.applicationKeypad) {
                    z = "\x1bOF";
                    break
                }
                z = "\x1bOF";
                break;
            case 33:
                if (A.shiftKey) {
                    this.scrollDisp(-(this.rows - 1));
                    return d(A)
                } else {
                    z = "\x1b[5~"
                }
                break;
            case 34:
                if (A.shiftKey) {
                    this.scrollDisp(this.rows - 1);
                    return d(A)
                } else {
                    z = "\x1b[6~"
                }
                break;
            case 112:
                z = "\x1bOP";
                break;
            case 113:
                z = "\x1bOQ";
                break;
            case 114:
                z = "\x1bOR";
                break;
            case 115:
                z = "\x1bOS";
                break;
            case 116:
                z = "\x1b[15~";
                break;
            case 117:
                z = "\x1b[17~";
                break;
            case 118:
                z = "\x1b[18~";
                break;
            case 119:
                z = "\x1b[19~";
                break;
            case 120:
                z = "\x1b[20~";
                break;
            case 121:
                z = "\x1b[21~";
                break;
            case 122:
                z = "\x1b[23~";
                break;
            case 123:
                z = "\x1b[24~";
                break;
            default:
                if (A.ctrlKey) {
                    if (A.keyCode >= 65 && A.keyCode <= 90) {
                        if (this.screenKeys) {
                            if (!this.prefixMode && !this.selectMode && A.keyCode === 65) {
                                this.enterPrefix();
                                return d(A)
                            }
                        }
                        if (this.prefixMode && A.keyCode === 86) {
                            this.leavePrefix();
                            return
                        }
                        if ((this.prefixMode || this.selectMode) && A.keyCode === 67) {
                            if (this.visualMode) {
                                b(function () {
                                    y.leaveVisual()
                                }, 1)
                            }
                            return
                        }
                        z = u.fromCharCode(A.keyCode - 64)
                    } else {
                        if (A.keyCode === 32) {
                            z = u.fromCharCode(0)
                        } else {
                            if (A.keyCode >= 51 && A.keyCode <= 55) {
                                z = u.fromCharCode(A.keyCode - 51 + 27)
                            } else {
                                if (A.keyCode === 56) {
                                    z = u.fromCharCode(127)
                                } else {
                                    if (A.keyCode === 219) {
                                        z = u.fromCharCode(27)
                                    } else {
                                        if (A.keyCode === 221) {
                                            z = u.fromCharCode(29)
                                        }
                                    }
                                }
                            }
                        }
                    }
                } else {
                    if ((!this.isMac && A.altKey) || (this.isMac && A.metaKey)) {
                        if (A.keyCode >= 65 && A.keyCode <= 90) {
                            z = "\x1b" + u.fromCharCode(A.keyCode + 32)
                        } else {
                            if (A.keyCode === 192) {
                                z = "\x1b`"
                            } else {
                                if (A.keyCode >= 48 && A.keyCode <= 57) {
                                    z = "\x1b" + (A.keyCode - 48)
                                }
                            }
                        }
                    }
                }
                break
        }
        if (!z) {
            return true
        }
        if (this.prefixMode) {
            this.leavePrefix();
            return d(A)
        }
        if (this.selectMode) {
            this.keySelect(A, z);
            return d(A)
        }
        this.emit("keydown", A);
        this.emit("key", z, A);
        this.showCursor();
        this.handler(z);
        return d(A)
    };
    t.prototype.setgLevel = function (y) {
        this.glevel = y;
        this.charset = this.charsets[y]
    };
    t.prototype.setgCharset = function (y, z) {
        this.charsets[y] = z;
        if (this.glevel === y) {
            this.charset = z
        }
    };
    t.prototype.keyPress = function (z) {
        var y;
        d(z);
        if (z.charCode) {
            y = z.charCode
        } else {
            if (z.which == null) {
                y = z.keyCode
            } else {
                if (z.which !== 0 && z.charCode !== 0) {
                    y = z.which
                } else {
                    return false
                }
            }
        }
        if (!y || z.ctrlKey || z.altKey || z.metaKey) {
            return false
        }
        y = u.fromCharCode(y);
        if (this.prefixMode) {
            this.leavePrefix();
            this.keyPrefix(z, y);
            return false
        }
        if (this.selectMode) {
            this.keySelect(z, y);
            return false
        }
        this.emit("keypress", y, z);
        this.emit("key", y, z);
        this.showCursor();
        this.handler(y);
        return false
    };
    t.prototype.send = function (z) {
        var y = this;
        if (!this.queue) {
            b(function () {
                y.handler(y.queue);
                y.queue = ""
            }, 1)
        }
        this.queue += z
    };
    t.prototype.bell = function () {
        this.emit("bell");
        if (!this.visualBell) {
            return
        }
        var y = this;
        this.element.style.borderColor = "white";
        b(function () {
            y.element.style.borderColor = ""
        }, 10);
        if (this.popOnBell) {
            this.focus()
        }
    };
    t.prototype.log = function () {
        if (!this.debug) {
            return
        }
        if (!this.context.console || !this.context.console.log) {
            return
        }
        var y = Array.prototype.slice.call(arguments);
        this.context.console.log.apply(this.context.console, y)
    };
    t.prototype.error = function () {
        if (!this.debug) {
            return
        }
        if (!this.context.console || !this.context.console.error) {
            return
        }
        var y = Array.prototype.slice.call(arguments);
        this.context.console.error.apply(this.context.console, y)
    };
    t.prototype.resize = function (z, F) {
        var A, E, C, B, D;
        if (z < 1) {
            z = 1
        }
        if (F < 1) {
            F = 1
        }
        B = this.cols;
        if (B < z) {
            D = [this.defAttr, " "];
            C = this.lines.length;
            while (C--) {
                while (this.lines[C].length < z) {
                    this.lines[C].push(D)
                }
            }
        } else {
            if (B > z) {
                C = this.lines.length;
                while (C--) {
                    while (this.lines[C].length > z) {
                        this.lines[C].pop()
                    }
                }
            }
        }
        this.setupStops(B);
        this.cols = z;
        B = this.rows;
        if (B < F) {
            E = this.element;
            while (B++ < F) {
                if (this.lines.length < F + this.ybase) {
                    this.lines.push(this.blankLine())
                }
                if (this.children.length < F) {
                    A = this.document.createElement("div");
                    E.appendChild(A);
                    this.children.push(A)
                }
            }
        } else {
            if (B > F) {
                while (B-- > F) {
                    if (this.lines.length > F + this.ybase) {
                        this.lines.pop()
                    }
                    if (this.children.length > F) {
                        E = this.children.pop();
                        if (!E) {
                            continue
                        }
                        E.parentNode.removeChild(E)
                    }
                }
            }
        }
        this.rows = F;
        if (this.y >= F) {
            this.y = F - 1
        }
        if (this.x >= z) {
            this.x = z - 1
        }
        this.scrollTop = 0;
        this.scrollBottom = F - 1;
        this.refresh(0, this.rows - 1);
        this.normal = null
    };
    t.prototype.updateRange = function (z) {
        if (z < this.refreshStart) {
            this.refreshStart = z
        }
        if (z > this.refreshEnd) {
            this.refreshEnd = z
        }
    };
    t.prototype.maxRange = function () {
        this.refreshStart = 0;
        this.refreshEnd = this.rows - 1
    };
    t.prototype.setupStops = function (y) {
        if (y != null) {
            if (!this.tabs[y]) {
                y = this.prevStop(y)
            }
        } else {
            this.tabs = {};
            y = 0
        }
        for (; y < this.cols; y += 8) {
            this.tabs[y] = true
        }
    };
    t.prototype.prevStop = function (y) {
        if (y == null) {
            y = this.x
        }
        while (!this.tabs[--y] && y > 0) {
        }
        return y >= this.cols ? this.cols - 1 : y < 0 ? 0 : y
    };
    t.prototype.nextStop = function (y) {
        if (y == null) {
            y = this.x
        }
        while (!this.tabs[++y] && y < this.cols) {
        }
        return y >= this.cols ? this.cols - 1 : y < 0 ? 0 : y
    };
    t.prototype.eraseRight = function (z, C) {
        var A = this.lines[this.ybase + C], B = [this.eraseAttr(), " "];
        for (; z < this.cols; z++) {
            A[z] = B
        }
        this.updateRange(C)
    };
    t.prototype.eraseLeft = function (z, C) {
        var A = this.lines[this.ybase + C], B = [this.eraseAttr(), " "];
        z++;
        while (z--) {
            A[z] = B
        }
        this.updateRange(C)
    };
    t.prototype.eraseLine = function (z) {
        this.eraseRight(0, z)
    };
    t.prototype.blankLine = function (C) {
        var y = C ? this.eraseAttr() : this.defAttr;
        var B = [y, " "], z = [], A = 0;
        for (; A < this.cols; A++) {
            z[A] = B
        }
        return z
    };
    t.prototype.ch = function (y) {
        return y ? [this.eraseAttr(), " "] : [this.defAttr, " "]
    };
    t.prototype.is = function (z) {
        var y = this.termName;
        return (y + "").indexOf(z) === 0
    };
    t.prototype.handler = function (y) {
        this.emit("data", y)
    };
    t.prototype.handleTitle = function (y) {
        this.emit("title", y)
    };
    t.prototype.index = function () {
        this.y++;
        if (this.y > this.scrollBottom) {
            this.y--;
            this.scroll()
        }
        this.state = x
    };
    t.prototype.reverseIndex = function () {
        var y;
        this.y--;
        if (this.y < this.scrollTop) {
            this.y++;
            this.lines.splice(this.y + this.ybase, 0, this.blankLine(true));
            y = this.rows - 1 - this.scrollBottom;
            this.lines.splice(this.rows - 1 + this.ybase - y + 1, 1);
            this.updateRange(this.scrollTop);
            this.updateRange(this.scrollBottom)
        }
        this.state = x
    };
    t.prototype.reset = function () {
        this.options.rows = this.rows;
        this.options.cols = this.cols;
        t.call(this, this.options);
        this.refresh(0, this.rows - 1)
    };
    t.prototype.tabSet = function () {
        this.tabs[this.x] = true;
        this.state = x
    };
    t.prototype.cursorUp = function (z) {
        var y = z[0];
        if (y < 1) {
            y = 1
        }
        this.y -= y;
        if (this.y < 0) {
            this.y = 0
        }
    };
    t.prototype.cursorDown = function (z) {
        var y = z[0];
        if (y < 1) {
            y = 1
        }
        this.y += y;
        if (this.y >= this.rows) {
            this.y = this.rows - 1
        }
    };
    t.prototype.cursorForward = function (z) {
        var y = z[0];
        if (y < 1) {
            y = 1
        }
        this.x += y;
        if (this.x >= this.cols) {
            this.x = this.cols - 1
        }
    };
    t.prototype.cursorBackward = function (z) {
        var y = z[0];
        if (y < 1) {
            y = 1
        }
        this.x -= y;
        if (this.x < 0) {
            this.x = 0
        }
    };
    t.prototype.cursorPos = function (A) {
        var z, y;
        z = A[0] - 1;
        if (A.length >= 2) {
            y = A[1] - 1
        } else {
            y = 0
        }
        if (z < 0) {
            z = 0
        } else {
            if (z >= this.rows) {
                z = this.rows - 1
            }
        }
        if (y < 0) {
            y = 0
        } else {
            if (y >= this.cols) {
                y = this.cols - 1
            }
        }
        this.x = y;
        this.y = z
    };
    t.prototype.eraseInDisplay = function (z) {
        var y;
        switch (z[0]) {
            case 0:
                this.eraseRight(this.x, this.y);
                y = this.y + 1;
                for (; y < this.rows; y++) {
                    this.eraseLine(y)
                }
                break;
            case 1:
                this.eraseLeft(this.x, this.y);
                y = this.y;
                while (y--) {
                    this.eraseLine(y)
                }
                break;
            case 2:
                y = this.rows;
                while (y--) {
                    this.eraseLine(y)
                }
                break;
            case 3:
                break
        }
    };
    t.prototype.eraseInLine = function (y) {
        switch (y[0]) {
            case 0:
                this.eraseRight(this.x, this.y);
                break;
            case 1:
                this.eraseLeft(this.x, this.y);
                break;
            case 2:
                this.eraseLine(this.y);
                break
        }
    };
    t.prototype.charAttributes = function (E) {
        if (E.length === 1 && E[0] === 0) {
            this.curAttr = this.defAttr;
            return
        }
        var A = E.length, C = 0, z = this.curAttr >> 18, y = (this.curAttr >> 9) & 511, B = this.curAttr & 511, D;
        for (; C < A; C++) {
            D = E[C];
            if (D >= 30 && D <= 37) {
                y = D - 30
            } else {
                if (D >= 40 && D <= 47) {
                    B = D - 40
                } else {
                    if (D >= 90 && D <= 97) {
                        D += 8;
                        y = D - 90
                    } else {
                        if (D >= 100 && D <= 107) {
                            D += 8;
                            B = D - 100
                        } else {
                            if (D === 0) {
                                z = this.defAttr >> 18;
                                y = (this.defAttr >> 9) & 511;
                                B = this.defAttr & 511
                            } else {
                                if (D === 1) {
                                    z |= 1
                                } else {
                                    if (D === 4) {
                                        z |= 2
                                    } else {
                                        if (D === 5) {
                                            z |= 4
                                        } else {
                                            if (D === 7) {
                                                z |= 8
                                            } else {
                                                if (D === 8) {
                                                    z |= 16
                                                } else {
                                                    if (D === 22) {
                                                        z &= ~1
                                                    } else {
                                                        if (D === 24) {
                                                            z &= ~2
                                                        } else {
                                                            if (D === 25) {
                                                                z &= ~4
                                                            } else {
                                                                if (D === 27) {
                                                                    z &= ~8
                                                                } else {
                                                                    if (D === 28) {
                                                                        z &= ~16
                                                                    } else {
                                                                        if (D === 39) {
                                                                            y = (this.defAttr >> 9) & 511
                                                                        } else {
                                                                            if (D === 49) {
                                                                                B = this.defAttr & 511
                                                                            } else {
                                                                                if (D === 38) {
                                                                                    if (E[C + 1] === 2) {
                                                                                        C += 2;
                                                                                        y = m(E[C] & 255, E[C + 1] & 255, E[C + 2] & 255);
                                                                                        if (y === -1) {
                                                                                            y = 511
                                                                                        }
                                                                                        C += 2
                                                                                    } else {
                                                                                        if (E[C + 1] === 5) {
                                                                                            C += 2;
                                                                                            D = E[C] & 255;
                                                                                            y = D
                                                                                        }
                                                                                    }
                                                                                } else {
                                                                                    if (D === 48) {
                                                                                        if (E[C + 1] === 2) {
                                                                                            C += 2;
                                                                                            B = m(E[C] & 255, E[C + 1] & 255, E[C + 2] & 255);
                                                                                            if (B === -1) {
                                                                                                B = 511
                                                                                            }
                                                                                            C += 2
                                                                                        } else {
                                                                                            if (E[C + 1] === 5) {
                                                                                                C += 2;
                                                                                                D = E[C] & 255;
                                                                                                B = D
                                                                                            }
                                                                                        }
                                                                                    } else {
                                                                                        if (D === 100) {
                                                                                            y = (this.defAttr >> 9) & 511;
                                                                                            B = this.defAttr & 511
                                                                                        } else {
                                                                                            this.error("Unknown SGR attribute: %d.", D)
                                                                                        }
                                                                                    }
                                                                                }
                                                                            }
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        this.curAttr = (z << 18) | (y << 9) | B
    };
    t.prototype.deviceStatus = function (y) {
        if (!this.prefix) {
            switch (y[0]) {
                case 5:
                    this.send("\x1b[0n");
                    break;
                case 6:
                    this.send("\x1b[" + (this.y + 1) + ";" + (this.x + 1) + "R");
                    break
            }
        } else {
            if (this.prefix === "?") {
                switch (y[0]) {
                    case 6:
                        this.send("\x1b[?" + (this.y + 1) + ";" + (this.x + 1) + "R");
                        break;
                    case 15:
                        break;
                    case 25:
                        break;
                    case 26:
                        break;
                    case 53:
                        break
                }
            }
        }
    };
    t.prototype.insertChars = function (C) {
        var B, A, y, z;
        B = C[0];
        if (B < 1) {
            B = 1
        }
        A = this.y + this.ybase;
        y = this.x;
        z = [this.eraseAttr(), " "];
        while (B-- && y < this.cols) {
            this.lines[A].splice(y++, 0, z);
            this.lines[A].pop()
        }
    };
    t.prototype.cursorNextLine = function (z) {
        var y = z[0];
        if (y < 1) {
            y = 1
        }
        this.y += y;
        if (this.y >= this.rows) {
            this.y = this.rows - 1
        }
        this.x = 0
    };
    t.prototype.cursorPrecedingLine = function (z) {
        var y = z[0];
        if (y < 1) {
            y = 1
        }
        this.y -= y;
        if (this.y < 0) {
            this.y = 0
        }
        this.x = 0
    };
    t.prototype.cursorCharAbsolute = function (z) {
        var y = z[0];
        if (y < 1) {
            y = 1
        }
        this.x = y - 1
    };
    t.prototype.insertLines = function (B) {
        var A, z, y;
        A = B[0];
        if (A < 1) {
            A = 1
        }
        z = this.y + this.ybase;
        y = this.rows - 1 - this.scrollBottom;
        y = this.rows - 1 + this.ybase - y + 1;
        while (A--) {
            this.lines.splice(z, 0, this.blankLine(true));
            this.lines.splice(y, 1)
        }
        this.updateRange(this.y);
        this.updateRange(this.scrollBottom)
    };
    t.prototype.deleteLines = function (B) {
        var A, z, y;
        A = B[0];
        if (A < 1) {
            A = 1
        }
        z = this.y + this.ybase;
        y = this.rows - 1 - this.scrollBottom;
        y = this.rows - 1 + this.ybase - y;
        while (A--) {
            this.lines.splice(y + 1, 0, this.blankLine(true));
            this.lines.splice(z, 1)
        }
        this.updateRange(this.y);
        this.updateRange(this.scrollBottom)
    };
    t.prototype.deleteChars = function (B) {
        var A, z, y;
        A = B[0];
        if (A < 1) {
            A = 1
        }
        z = this.y + this.ybase;
        y = [this.eraseAttr(), " "];
        while (A--) {
            this.lines[z].splice(this.x, 1);
            this.lines[z].push(y)
        }
    };
    t.prototype.eraseChars = function (C) {
        var B, A, y, z;
        B = C[0];
        if (B < 1) {
            B = 1
        }
        A = this.y + this.ybase;
        y = this.x;
        z = [this.eraseAttr(), " "];
        while (B-- && y < this.cols) {
            this.lines[A][y++] = z
        }
    };
    t.prototype.charPosAbsolute = function (z) {
        var y = z[0];
        if (y < 1) {
            y = 1
        }
        this.x = y - 1;
        if (this.x >= this.cols) {
            this.x = this.cols - 1
        }
    };
    t.prototype.HPositionRelative = function (z) {
        var y = z[0];
        if (y < 1) {
            y = 1
        }
        this.x += y;
        if (this.x >= this.cols) {
            this.x = this.cols - 1
        }
    };
    t.prototype.sendDeviceAttributes = function (y) {
        if (y[0] > 0) {
            return
        }
        if (!this.prefix) {
            if (this.is("xterm") || this.is("rxvt-unicode") || this.is("screen")) {
                this.send("\x1b[?1;2c")
            } else {
                if (this.is("linux")) {
                    this.send("\x1b[?6c")
                }
            }
        } else {
            if (this.prefix === ">") {
                if (this.is("xterm")) {
                    this.send("\x1b[>0;276;0c")
                } else {
                    if (this.is("rxvt-unicode")) {
                        this.send("\x1b[>85;95;0c")
                    } else {
                        if (this.is("linux")) {
                            this.send(y[0] + "c")
                        } else {
                            if (this.is("screen")) {
                                this.send("\x1b[>83;40003;0c")
                            }
                        }
                    }
                }
            }
        }
    };
    t.prototype.linePosAbsolute = function (z) {
        var y = z[0];
        if (y < 1) {
            y = 1
        }
        this.y = y - 1;
        if (this.y >= this.rows) {
            this.y = this.rows - 1
        }
    };
    t.prototype.VPositionRelative = function (z) {
        var y = z[0];
        if (y < 1) {
            y = 1
        }
        this.y += y;
        if (this.y >= this.rows) {
            this.y = this.rows - 1
        }
    };
    t.prototype.HVPosition = function (y) {
        if (y[0] < 1) {
            y[0] = 1
        }
        if (y[1] < 1) {
            y[1] = 1
        }
        this.y = y[0] - 1;
        if (this.y >= this.rows) {
            this.y = this.rows - 1
        }
        this.x = y[1] - 1;
        if (this.x >= this.cols) {
            this.x = this.cols - 1
        }
    };
    t.prototype.setMode = function (B) {
        if (typeof B === "object") {
            var y = B.length, z = 0;
            for (; z < y; z++) {
                this.setMode(B[z])
            }
            return
        }
        if (!this.prefix) {
            switch (B) {
                case 4:
                    this.insertMode = true;
                    break;
                case 20:
                    break
            }
        } else {
            if (this.prefix === "?") {
                switch (B) {
                    case 1:
                        this.applicationCursor = true;
                        break;
                    case 2:
                        this.setgCharset(0, t.charsets.US);
                        this.setgCharset(1, t.charsets.US);
                        this.setgCharset(2, t.charsets.US);
                        this.setgCharset(3, t.charsets.US);
                        break;
                    case 3:
                        this.savedCols = this.cols;
                        this.resize(132, this.rows);
                        break;
                    case 6:
                        this.originMode = true;
                        break;
                    case 7:
                        this.wraparoundMode = true;
                        break;
                    case 12:
                        break;
                    case 66:
                        this.log("Serial port requested application keypad.");
                        this.applicationKeypad = true;
                        break;
                    case 9:
                    case 1000:
                    case 1002:
                    case 1003:
                        this.x10Mouse = B === 9;
                        this.vt200Mouse = B === 1000;
                        this.normalMouse = B > 1000;
                        this.mouseEvents = true;
                        this.element.style.cursor = "default";
                        this.log("Binding to mouse events.");
                        break;
                    case 1004:
                        this.sendFocus = true;
                        break;
                    case 1005:
                        this.utfMouse = true;
                        break;
                    case 1006:
                        this.sgrMouse = true;
                        break;
                    case 1015:
                        this.urxvtMouse = true;
                        break;
                    case 25:
                        this.cursorHidden = false;
                        break;
                    case 1049:
                    case 47:
                    case 1047:
                        if (!this.normal) {
                            var A = {
                                lines: this.lines,
                                ybase: this.ybase,
                                ydisp: this.ydisp,
                                x: this.x,
                                y: this.y,
                                scrollTop: this.scrollTop,
                                scrollBottom: this.scrollBottom,
                                tabs: this.tabs
                            };
                            this.reset();
                            this.normal = A;
                            this.showCursor()
                        }
                        break
                }
            }
        }
    };
    t.prototype.resetMode = function (A) {
        if (typeof A === "object") {
            var y = A.length, z = 0;
            for (; z < y; z++) {
                this.resetMode(A[z])
            }
            return
        }
        if (!this.prefix) {
            switch (A) {
                case 4:
                    this.insertMode = false;
                    break;
                case 20:
                    break
            }
        } else {
            if (this.prefix === "?") {
                switch (A) {
                    case 1:
                        this.applicationCursor = false;
                        break;
                    case 3:
                        if (this.cols === 132 && this.savedCols) {
                            this.resize(this.savedCols, this.rows)
                        }
                        delete this.savedCols;
                        break;
                    case 6:
                        this.originMode = false;
                        break;
                    case 7:
                        this.wraparoundMode = false;
                        break;
                    case 12:
                        break;
                    case 66:
                        this.log("Switching back to normal keypad.");
                        this.applicationKeypad = false;
                        break;
                    case 9:
                    case 1000:
                    case 1002:
                    case 1003:
                        this.x10Mouse = false;
                        this.vt200Mouse = false;
                        this.normalMouse = false;
                        this.mouseEvents = false;
                        this.element.style.cursor = "";
                        break;
                    case 1004:
                        this.sendFocus = false;
                        break;
                    case 1005:
                        this.utfMouse = false;
                        break;
                    case 1006:
                        this.sgrMouse = false;
                        break;
                    case 1015:
                        this.urxvtMouse = false;
                        break;
                    case 25:
                        this.cursorHidden = true;
                        break;
                    case 1049:
                    case 47:
                    case 1047:
                        if (this.normal) {
                            this.lines = this.normal.lines;
                            this.ybase = this.normal.ybase;
                            this.ydisp = this.normal.ydisp;
                            this.x = this.normal.x;
                            this.y = this.normal.y;
                            this.scrollTop = this.normal.scrollTop;
                            this.scrollBottom = this.normal.scrollBottom;
                            this.tabs = this.normal.tabs;
                            this.normal = null;
                            this.refresh(0, this.rows - 1);
                            this.showCursor()
                        }
                        break
                }
            }
        }
    };
    t.prototype.setScrollRegion = function (y) {
        if (this.prefix) {
            return
        }
        this.scrollTop = (y[0] || 1) - 1;
        this.scrollBottom = (y[1] || this.rows) - 1;
        this.x = 0;
        this.y = 0
    };
    t.prototype.saveCursor = function (y) {
        this.savedX = this.x;
        this.savedY = this.y
    };
    t.prototype.restoreCursor = function (y) {
        this.x = this.savedX || 0;
        this.y = this.savedY || 0
    };
    t.prototype.cursorForwardTab = function (z) {
        var y = z[0] || 1;
        while (y--) {
            this.x = this.nextStop()
        }
    };
    t.prototype.scrollUp = function (z) {
        var y = z[0] || 1;
        while (y--) {
            this.lines.splice(this.ybase + this.scrollTop, 1);
            this.lines.splice(this.ybase + this.scrollBottom, 0, this.blankLine())
        }
        this.updateRange(this.scrollTop);
        this.updateRange(this.scrollBottom)
    };
    t.prototype.scrollDown = function (z) {
        var y = z[0] || 1;
        while (y--) {
            this.lines.splice(this.ybase + this.scrollBottom, 1);
            this.lines.splice(this.ybase + this.scrollTop, 0, this.blankLine())
        }
        this.updateRange(this.scrollTop);
        this.updateRange(this.scrollBottom)
    };
    t.prototype.initMouseTracking = function (y) {
    };
    t.prototype.resetTitleModes = function (y) {
    };
    t.prototype.cursorBackwardTab = function (z) {
        var y = z[0] || 1;
        while (y--) {
            this.x = this.prevStop()
        }
    };
    t.prototype.repeatPrecedingCharacter = function (B) {
        var A = B[0] || 1, y = this.lines[this.ybase + this.y], z = y[this.x - 1] || [this.defAttr, " "];
        while (A--) {
            y[this.x++] = z
        }
    };
    t.prototype.tabClear = function (z) {
        var y = z[0];
        if (y <= 0) {
            delete this.tabs[this.x]
        } else {
            if (y === 3) {
                this.tabs = {}
            }
        }
    };
    t.prototype.mediaCopy = function (y) {
    };
    t.prototype.setResources = function (y) {
    };
    t.prototype.disableModifiers = function (y) {
    };
    t.prototype.setPointerMode = function (y) {
    };
    t.prototype.softReset = function (y) {
        this.cursorHidden = false;
        this.insertMode = false;
        this.originMode = false;
        this.wraparoundMode = false;
        this.applicationKeypad = false;
        this.applicationCursor = false;
        this.scrollTop = 0;
        this.scrollBottom = this.rows - 1;
        this.curAttr = this.defAttr;
        this.x = this.y = 0;
        this.charset = null;
        this.glevel = 0;
        this.charsets = [null]
    };
    t.prototype.requestAnsiMode = function (y) {
    };
    t.prototype.requestPrivateMode = function (y) {
    };
    t.prototype.setConformanceLevel = function (y) {
    };
    t.prototype.loadLEDs = function (y) {
    };
    t.prototype.setCursorStyle = function (y) {
    };
    t.prototype.setCharProtectionAttr = function (y) {
    };
    t.prototype.restorePrivateValues = function (y) {
    };
    t.prototype.setAttrInRectangle = function (F) {
        var D = F[0], B = F[1], z = F[2], E = F[3], y = F[4];
        var A, C;
        for (; D < z + 1; D++) {
            A = this.lines[this.ybase + D];
            for (C = B; C < E; C++) {
                A[C] = [y, A[C][1]]
            }
        }
        this.updateRange(F[0]);
        this.updateRange(F[2])
    };
    t.prototype.savePrivateValues = function (y) {
    };
    t.prototype.manipulateWindow = function (y) {
    };
    t.prototype.reverseAttrInRectangle = function (y) {
    };
    t.prototype.setTitleModeFeature = function (y) {
    };
    t.prototype.setWarningBellVolume = function (y) {
    };
    t.prototype.setMarginBellVolume = function (y) {
    };
    t.prototype.copyRectangle = function (y) {
    };
    t.prototype.enableFilterRectangle = function (y) {
    };
    t.prototype.requestParameters = function (y) {
    };
    t.prototype.selectChangeExtent = function (y) {
    };
    t.prototype.fillRectangle = function (F) {
        var D = F[0], C = F[1], A = F[2], y = F[3], E = F[4];
        var z, B;
        for (; C < y + 1; C++) {
            z = this.lines[this.ybase + C];
            for (B = A; B < E; B++) {
                z[B] = [z[B][0], u.fromCharCode(D)]
            }
        }
        this.updateRange(F[1]);
        this.updateRange(F[3])
    };
    t.prototype.enableLocatorReporting = function (z) {
        var y = z[0] > 0
    };
    t.prototype.eraseRectangle = function (F) {
        var C = F[0], A = F[1], y = F[2], E = F[3];
        var z, B, D;
        D = [this.eraseAttr(), " "];
        for (; C < y + 1; C++) {
            z = this.lines[this.ybase + C];
            for (B = A; B < E; B++) {
                z[B] = D
            }
        }
        this.updateRange(F[0]);
        this.updateRange(F[2])
    };
    t.prototype.setLocatorEvents = function (y) {
    };
    t.prototype.selectiveEraseRectangle = function (y) {
    };
    t.prototype.requestLocatorPosition = function (y) {
    };
    t.prototype.insertColumns = function () {
        var B = params[0], y = this.ybase + this.rows, A = [this.eraseAttr(), " "], z;
        while (B--) {
            for (z = this.ybase; z < y; z++) {
                this.lines[z].splice(this.x + 1, 0, A);
                this.lines[z].pop()
            }
        }
        this.maxRange()
    };
    t.prototype.deleteColumns = function () {
        var B = params[0], y = this.ybase + this.rows, A = [this.eraseAttr(), " "], z;
        while (B--) {
            for (z = this.ybase; z < y; z++) {
                this.lines[z].splice(this.x, 1);
                this.lines[z].push(A)
            }
        }
        this.maxRange()
    };
    t.prototype.enterPrefix = function () {
        this.prefixMode = true
    };
    t.prototype.leavePrefix = function () {
        this.prefixMode = false
    };
    t.prototype.enterSelect = function () {
        this._real = {
            x: this.x,
            y: this.y,
            ydisp: this.ydisp,
            ybase: this.ybase,
            cursorHidden: this.cursorHidden,
            lines: this.copyBuffer(this.lines),
            write: this.write
        };
        this.write = function () {
        };
        this.selectMode = true;
        this.visualMode = false;
        this.cursorHidden = false;
        this.refresh(this.y, this.y)
    };
    t.prototype.leaveSelect = function () {
        this.x = this._real.x;
        this.y = this._real.y;
        this.ydisp = this._real.ydisp;
        this.ybase = this._real.ybase;
        this.cursorHidden = this._real.cursorHidden;
        this.lines = this._real.lines;
        this.write = this._real.write;
        delete this._real;
        this.selectMode = false;
        this.visualMode = false;
        this.refresh(0, this.rows - 1)
    };
    t.prototype.enterVisual = function () {
        this._real.preVisual = this.copyBuffer(this.lines);
        this.selectText(this.x, this.x, this.ydisp + this.y, this.ydisp + this.y);
        this.visualMode = true
    };
    t.prototype.leaveVisual = function () {
        this.lines = this._real.preVisual;
        delete this._real.preVisual;
        delete this._selected;
        this.visualMode = false;
        this.refresh(0, this.rows - 1)
    };
    t.prototype.enterSearch = function (A) {
        this.entry = "";
        this.searchMode = true;
        this.searchDown = A;
        this._real.preSearch = this.copyBuffer(this.lines);
        this._real.preSearchX = this.x;
        this._real.preSearchY = this.y;
        var y = this.ydisp + this.rows - 1;
        for (var z = 0; z < this.entryPrefix.length; z++) {
            this.lines[y][z] = [(this.defAttr & ~511) | 4, this.entryPrefix[z]]
        }
        this.y = this.rows - 1;
        this.x = this.entryPrefix.length;
        this.refresh(this.rows - 1, this.rows - 1)
    };
    t.prototype.leaveSearch = function () {
        this.searchMode = false;
        if (this._real.preSearch) {
            this.lines = this._real.preSearch;
            this.x = this._real.preSearchX;
            this.y = this._real.preSearchY;
            delete this._real.preSearch;
            delete this._real.preSearchX;
            delete this._real.preSearchY
        }
        this.refresh(this.rows - 1, this.rows - 1)
    };
    t.prototype.copyBuffer = function (A) {
        var A = A || this.lines, B = [];
        for (var C = 0; C < A.length; C++) {
            B[C] = [];
            for (var z = 0; z < A[C].length; z++) {
                B[C][z] = [A[C][z][0], A[C][z][1]]
            }
        }
        return B
    };
    t.prototype.getCopyTextarea = function (A) {
        var z = this._copyTextarea, y = this.document;
        if (!z) {
            z = y.createElement("textarea");
            z.style.position = "absolute";
            z.style.left = "-32000px";
            z.style.top = "-32000px";
            z.style.width = "0px";
            z.style.height = "0px";
            z.style.opacity = "0";
            z.style.backgroundColor = "transparent";
            z.style.borderStyle = "none";
            z.style.outlineStyle = "none";
            y.getElementsByTagName("body")[0].appendChild(z);
            this._copyTextarea = z
        }
        return z
    };
    t.prototype.copyText = function (A) {
        var z = this, y = this.getCopyTextarea();
        this.emit("copy", A);
        y.focus();
        y.textContent = A;
        y.value = A;
        y.setSelectionRange(0, A.length);
        b(function () {
            z.element.focus();
            z.focus()
        }, 1)
    };
    t.prototype.selectText = function (A, z, J, H) {
        var C, B, L, K, D, I, G, E, F;
        if (this._selected) {
            C = this._selected.x1;
            B = this._selected.x2;
            L = this._selected.y1;
            K = this._selected.y2;
            if (K < L) {
                D = B;
                B = C;
                C = D;
                D = K;
                K = L;
                L = D
            }
            if (B < C && L === K) {
                D = B;
                B = C;
                C = D
            }
            for (G = L; G <= K; G++) {
                I = 0;
                E = this.cols - 1;
                if (G === L) {
                    I = C
                }
                if (G === K) {
                    E = B
                }
                for (; I <= E; I++) {
                    if (this.lines[G][I].old != null) {
                        F = this.lines[G][I].old;
                        delete this.lines[G][I].old;
                        this.lines[G][I] = [F, this.lines[G][I][1]]
                    }
                }
            }
            J = this._selected.y1;
            A = this._selected.x1
        }
        J = Math.max(J, 0);
        J = Math.min(J, this.ydisp + this.rows - 1);
        H = Math.max(H, 0);
        H = Math.min(H, this.ydisp + this.rows - 1);
        this._selected = {x1: A, x2: z, y1: J, y2: H};
        if (H < J) {
            D = z;
            z = A;
            A = D;
            D = H;
            H = J;
            J = D
        }
        if (z < A && J === H) {
            D = z;
            z = A;
            A = D
        }
        for (G = J; G <= H; G++) {
            I = 0;
            E = this.cols - 1;
            if (G === J) {
                I = A
            }
            if (G === H) {
                E = z
            }
            for (; I <= E; I++) {
                F = this.lines[G][I][0];
                this.lines[G][I] = [(F & ~511) | ((511 << 9) | 4), this.lines[G][I][1]];
                this.lines[G][I].old = F
            }
        }
        J = J - this.ydisp;
        H = H - this.ydisp;
        J = Math.max(J, 0);
        J = Math.min(J, this.rows - 1);
        H = Math.max(H, 0);
        H = Math.min(H, this.rows - 1);
        this.refresh(0, this.rows - 1)
    };
    t.prototype.grabText = function (B, A, I, H) {
        var D = "", C = "", z, J, G, F, E;
        if (H < I) {
            E = A;
            A = B;
            B = E;
            E = H;
            H = I;
            I = E
        }
        if (A < B && I === H) {
            E = A;
            A = B;
            B = E
        }
        for (G = I; G <= H; G++) {
            J = 0;
            F = this.cols - 1;
            if (G === I) {
                J = B
            }
            if (G === H) {
                F = A
            }
            for (; J <= F; J++) {
                z = this.lines[G][J][1];
                if (z === " ") {
                    C += z;
                    continue
                }
                if (C) {
                    D += C;
                    C = ""
                }
                D += z;
                if (r(z)) {
                    J++
                }
            }
            C = "";
            D += "\n"
        }
        for (J = A, G = H; J < this.cols; J++) {
            if (this.lines[G][J][1] !== " ") {
                D = D.slice(0, -1);
                break
            }
        }
        return D
    };
    t.prototype.keyPrefix = function (z, y) {
        if (y === "k" || y === "&") {
            this.destroy()
        } else {
            if (y === "p" || y === "]") {
                this.emit("request paste")
            } else {
                if (y === "c") {
                    this.emit("request create")
                } else {
                    if (y >= "0" && y <= "9") {
                        y = +y - 1;
                        if (!~y) {
                            y = 9
                        }
                        this.emit("request term", y)
                    } else {
                        if (y === "n") {
                            this.emit("request term next")
                        } else {
                            if (y === "P") {
                                this.emit("request term previous")
                            } else {
                                if (y === ":") {
                                    this.emit("request command mode")
                                } else {
                                    if (y === "[") {
                                        this.enterSelect()
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    };
    t.prototype.keySelect = function (H, J) {
        this.showCursor();
        if (this.searchMode || J === "n" || J === "N") {
            return this.keySearch(H, J)
        }
        if (J === "\x04") {
            var G = this.ydisp + this.y;
            if (this.ydisp === this.ybase) {
                this.y = Math.min(this.y + (this.rows - 1) / 2 | 0, this.rows - 1);
                this.refresh(0, this.rows - 1)
            } else {
                this.scrollDisp((this.rows - 1) / 2 | 0)
            }
            if (this.visualMode) {
                this.selectText(this.x, this.x, G, this.ydisp + this.y)
            }
            return
        }
        if (J === "\x15") {
            var G = this.ydisp + this.y;
            if (this.ydisp === 0) {
                this.y = Math.max(this.y - (this.rows - 1) / 2 | 0, 0);
                this.refresh(0, this.rows - 1)
            } else {
                this.scrollDisp(-(this.rows - 1) / 2 | 0)
            }
            if (this.visualMode) {
                this.selectText(this.x, this.x, G, this.ydisp + this.y)
            }
            return
        }
        if (J === "\x06") {
            var G = this.ydisp + this.y;
            this.scrollDisp(this.rows - 1);
            if (this.visualMode) {
                this.selectText(this.x, this.x, G, this.ydisp + this.y)
            }
            return
        }
        if (J === "\x02") {
            var G = this.ydisp + this.y;
            this.scrollDisp(-(this.rows - 1));
            if (this.visualMode) {
                this.selectText(this.x, this.x, G, this.ydisp + this.y)
            }
            return
        }
        if (J === "k" || J === "\x1b[A") {
            var G = this.ydisp + this.y;
            this.y--;
            if (this.y < 0) {
                this.y = 0;
                this.scrollDisp(-1)
            }
            if (this.visualMode) {
                this.selectText(this.x, this.x, G, this.ydisp + this.y)
            } else {
                this.refresh(this.y, this.y + 1)
            }
            return
        }
        if (J === "j" || J === "\x1b[B") {
            var G = this.ydisp + this.y;
            this.y++;
            if (this.y >= this.rows) {
                this.y = this.rows - 1;
                this.scrollDisp(1)
            }
            if (this.visualMode) {
                this.selectText(this.x, this.x, G, this.ydisp + this.y)
            } else {
                this.refresh(this.y - 1, this.y)
            }
            return
        }
        if (J === "h" || J === "\x1b[D") {
            var I = this.x;
            this.x--;
            if (this.x < 0) {
                this.x = 0
            }
            if (this.visualMode) {
                this.selectText(I, this.x, this.ydisp + this.y, this.ydisp + this.y)
            } else {
                this.refresh(this.y, this.y)
            }
            return
        }
        if (J === "l" || J === "\x1b[C") {
            var I = this.x;
            this.x++;
            if (this.x >= this.cols) {
                this.x = this.cols - 1
            }
            if (this.visualMode) {
                this.selectText(I, this.x, this.ydisp + this.y, this.ydisp + this.y)
            } else {
                this.refresh(this.y, this.y)
            }
            return
        }
        if (J === "v" || J === " ") {
            if (!this.visualMode) {
                this.enterVisual()
            } else {
                this.leaveVisual()
            }
            return
        }
        if (J === "y") {
            if (this.visualMode) {
                var L = this.grabText(this._selected.x1, this._selected.x2, this._selected.y1, this._selected.y2);
                this.copyText(L);
                this.leaveVisual()
            }
            return
        }
        if (J === "q" || J === "\x1b") {
            if (this.visualMode) {
                this.leaveVisual()
            } else {
                this.leaveSelect()
            }
            return
        }
        if (J === "w" || J === "W") {
            var B = this.x;
            var A = this.y;
            var F = this.ydisp;
            var I = this.x;
            var G = this.y;
            var E = this.ydisp;
            var z = false;
            for (; ;) {
                var N = this.lines[E + G];
                while (I < this.cols) {
                    if (N[I][1] <= " ") {
                        z = true
                    } else {
                        if (z) {
                            break
                        }
                    }
                    I++
                }
                if (I >= this.cols) {
                    I = this.cols - 1
                }
                if (I === this.cols - 1 && N[I][1] <= " ") {
                    I = 0;
                    if (++G >= this.rows) {
                        G--;
                        if (++E > this.ybase) {
                            E = this.ybase;
                            I = this.x;
                            break
                        }
                    }
                    continue
                }
                break
            }
            this.x = I, this.y = G;
            this.scrollDisp(-this.ydisp + E);
            if (this.visualMode) {
                this.selectText(B, this.x, A + F, this.ydisp + this.y)
            }
            return
        }
        if (J === "b" || J === "B") {
            var B = this.x;
            var A = this.y;
            var F = this.ydisp;
            var I = this.x;
            var G = this.y;
            var E = this.ydisp;
            for (; ;) {
                var N = this.lines[E + G];
                var z = I > 0 && N[I][1] > " " && N[I - 1][1] > " ";
                while (I >= 0) {
                    if (N[I][1] <= " ") {
                        if (z && (I + 1 < this.cols && N[I + 1][1] > " ")) {
                            I++;
                            break
                        } else {
                            z = true
                        }
                    }
                    I--
                }
                if (I < 0) {
                    I = 0
                }
                if (I === 0 && (N[I][1] <= " " || !z)) {
                    I = this.cols - 1;
                    if (--G < 0) {
                        G++;
                        if (--E < 0) {
                            E++;
                            I = 0;
                            break
                        }
                    }
                    continue
                }
                break
            }
            this.x = I, this.y = G;
            this.scrollDisp(-this.ydisp + E);
            if (this.visualMode) {
                this.selectText(B, this.x, A + F, this.ydisp + this.y)
            }
            return
        }
        if (J === "e" || J === "E") {
            var I = this.x + 1;
            var G = this.y;
            var E = this.ydisp;
            if (I >= this.cols) {
                I--
            }
            for (; ;) {
                var N = this.lines[E + G];
                while (I < this.cols) {
                    if (N[I][1] <= " ") {
                        I++
                    } else {
                        break
                    }
                }
                while (I < this.cols) {
                    if (N[I][1] <= " ") {
                        if (I - 1 >= 0 && N[I - 1][1] > " ") {
                            I--;
                            break
                        }
                    }
                    I++
                }
                if (I >= this.cols) {
                    I = this.cols - 1
                }
                if (I === this.cols - 1 && N[I][1] <= " ") {
                    I = 0;
                    if (++G >= this.rows) {
                        G--;
                        if (++E > this.ybase) {
                            E = this.ybase;
                            break
                        }
                    }
                    continue
                }
                break
            }
            this.x = I, this.y = G;
            this.scrollDisp(-this.ydisp + E);
            if (this.visualMode) {
                this.selectText(B, this.x, A + F, this.ydisp + this.y)
            }
            return
        }
        if (J === "^" || J === "0") {
            var B = this.x;
            if (J === "0") {
                this.x = 0
            } else {
                if (J === "^") {
                    var N = this.lines[this.ydisp + this.y];
                    var I = 0;
                    while (I < this.cols) {
                        if (N[I][1] > " ") {
                            break
                        }
                        I++
                    }
                    if (I >= this.cols) {
                        I = this.cols - 1
                    }
                    this.x = I
                }
            }
            if (this.visualMode) {
                this.selectText(B, this.x, this.ydisp + this.y, this.ydisp + this.y)
            } else {
                this.refresh(this.y, this.y)
            }
            return
        }
        if (J === "$") {
            var B = this.x;
            var N = this.lines[this.ydisp + this.y];
            var I = this.cols - 1;
            while (I >= 0) {
                if (N[I][1] > " ") {
                    if (this.visualMode && I < this.cols - 1) {
                        I++
                    }
                    break
                }
                I--
            }
            if (I < 0) {
                I = 0
            }
            this.x = I;
            if (this.visualMode) {
                this.selectText(B, this.x, this.ydisp + this.y, this.ydisp + this.y)
            } else {
                this.refresh(this.y, this.y)
            }
            return
        }
        if (J === "g" || J === "G") {
            var B = this.x;
            var A = this.y;
            var F = this.ydisp;
            if (J === "g") {
                this.x = 0, this.y = 0;
                this.scrollDisp(-this.ydisp)
            } else {
                if (J === "G") {
                    this.x = 0, this.y = this.rows - 1;
                    this.scrollDisp(this.ybase)
                }
            }
            if (this.visualMode) {
                this.selectText(B, this.x, A + F, this.ydisp + this.y)
            }
            return
        }
        if (J === "H" || J === "M" || J === "L") {
            var B = this.x;
            var A = this.y;
            if (J === "H") {
                this.x = 0, this.y = 0
            } else {
                if (J === "M") {
                    this.x = 0, this.y = this.rows / 2 | 0
                } else {
                    if (J === "L") {
                        this.x = 0, this.y = this.rows - 1
                    }
                }
            }
            if (this.visualMode) {
                this.selectText(B, this.x, this.ydisp + A, this.ydisp + this.y)
            } else {
                this.refresh(A, A);
                this.refresh(this.y, this.y)
            }
            return
        }
        if (J === "{" || J === "}") {
            var B = this.x;
            var A = this.y;
            var F = this.ydisp;
            var N;
            var K = false;
            var M = false;
            var C = -1;
            var G = this.y + (J === "{" ? -1 : 1);
            var E = this.ydisp;
            var D;
            if (J === "{") {
                if (G < 0) {
                    G++;
                    if (E > 0) {
                        E--
                    }
                }
            } else {
                if (J === "}") {
                    if (G >= this.rows) {
                        G--;
                        if (E < this.ybase) {
                            E++
                        }
                    }
                }
            }
            for (; ;) {
                N = this.lines[E + G];
                for (D = 0; D < this.cols; D++) {
                    if (N[D][1] > " ") {
                        if (C === -1) {
                            C = 0
                        }
                        K = true;
                        break
                    } else {
                        if (D === this.cols - 1) {
                            if (C === -1) {
                                C = 1
                            } else {
                                if (C === 0) {
                                    M = true
                                } else {
                                    if (C === 1) {
                                        if (K) {
                                            M = true
                                        }
                                    }
                                }
                            }
                            break
                        }
                    }
                }
                if (M) {
                    break
                }
                if (J === "{") {
                    G--;
                    if (G < 0) {
                        G++;
                        if (E > 0) {
                            E--
                        } else {
                            break
                        }
                    }
                } else {
                    if (J === "}") {
                        G++;
                        if (G >= this.rows) {
                            G--;
                            if (E < this.ybase) {
                                E++
                            } else {
                                break
                            }
                        }
                    }
                }
            }
            if (!M) {
                if (J === "{") {
                    G = 0;
                    E = 0
                } else {
                    if (J === "}") {
                        G = this.rows - 1;
                        E = this.ybase
                    }
                }
            }
            this.x = 0, this.y = G;
            this.scrollDisp(-this.ydisp + E);
            if (this.visualMode) {
                this.selectText(B, this.x, A + F, this.ydisp + this.y)
            }
            return
        }
        if (J === "/" || J === "?") {
            if (!this.visualMode) {
                this.enterSearch(J === "/")
            }
            return
        }
        return false
    };
    t.prototype.keySearch = function (I, L) {
        if (L === "\x1b") {
            this.leaveSearch();
            return
        }
        if (L === "\r" || (!this.searchMode && (L === "n" || L === "N"))) {
            this.leaveSearch();
            var K = this.entry;
            if (!K) {
                this.refresh(0, this.rows - 1);
                return
            }
            var B = this.x;
            var A = this.y;
            var G = this.ydisp;
            var N;
            var M = false;
            var F = false;
            var J = this.x + 1;
            var H = this.ydisp + this.y;
            var E, D;
            var C = L === "N" ? this.searchDown : !this.searchDown;
            for (; ;) {
                N = this.lines[H];
                while (J < this.cols) {
                    for (D = 0; D < K.length; D++) {
                        if (J + D >= this.cols) {
                            break
                        }
                        if (N[J + D][1] !== K[D]) {
                            break
                        } else {
                            if (N[J + D][1] === K[D] && D === K.length - 1) {
                                M = true;
                                break
                            }
                        }
                    }
                    if (M) {
                        break
                    }
                    J += D + 1
                }
                if (M) {
                    break
                }
                J = 0;
                if (!C) {
                    H++;
                    if (H > this.ybase + this.rows - 1) {
                        if (F) {
                            break
                        }
                        F = true;
                        H = 0
                    }
                } else {
                    H--;
                    if (H < 0) {
                        if (F) {
                            break
                        }
                        F = true;
                        H = this.ybase + this.rows - 1
                    }
                }
            }
            if (M) {
                if (H - this.ybase < 0) {
                    E = H;
                    H = 0;
                    if (E > this.ybase) {
                        H = E - this.ybase;
                        E = this.ybase
                    }
                } else {
                    E = this.ybase;
                    H -= this.ybase
                }
                this.x = J, this.y = H;
                this.scrollDisp(-this.ydisp + E);
                if (this.visualMode) {
                    this.selectText(B, this.x, A + G, this.ydisp + this.y)
                }
                return
            }
            this.refresh(0, this.rows - 1);
            return
        }
        if (L === "\b" || L === "\x7f") {
            if (this.entry.length === 0) {
                return
            }
            var z = this.ydisp + this.rows - 1;
            this.entry = this.entry.slice(0, -1);
            var D = this.entryPrefix.length + this.entry.length;
            this.lines[z][D] = [this.lines[z][D][0], " "];
            this.x--;
            this.refresh(this.rows - 1, this.rows - 1);
            this.refresh(this.y, this.y);
            return
        }
        if (L.length === 1 && L >= " " && L <= "~") {
            var z = this.ydisp + this.rows - 1;
            this.entry += L;
            var D = this.entryPrefix.length + this.entry.length - 1;
            this.lines[z][D] = [(this.defAttr & ~511) | 4, L];
            this.x++;
            this.refresh(this.rows - 1, this.rows - 1);
            this.refresh(this.y, this.y);
            return
        }
        return false
    };
    t.charsets = {};
    t.charsets.SCLD = {
        "`": "\u25c6",
        a: "\u2592",
        b: "\u0009",
        c: "\u000c",
        d: "\u000d",
        e: "\u000a",
        f: "\u00b0",
        g: "\u00b1",
        h: "\u2424",
        i: "\u000b",
        j: "\u2518",
        k: "\u2510",
        l: "\u250c",
        m: "\u2514",
        n: "\u253c",
        o: "\u23ba",
        p: "\u23bb",
        q: "\u2500",
        r: "\u23bc",
        s: "\u23bd",
        t: "\u251c",
        u: "\u2524",
        v: "\u2534",
        w: "\u252c",
        x: "\u2502",
        y: "\u2264",
        z: "\u2265",
        "{": "\u03c0",
        "|": "\u2260",
        "}": "\u00a3",
        "~": "\u00b7"
    };
    t.charsets.UK = null;
    t.charsets.US = null;
    t.charsets.Dutch = null;
    t.charsets.Finnish = null;
    t.charsets.French = null;
    t.charsets.FrenchCanadian = null;
    t.charsets.German = null;
    t.charsets.Italian = null;
    t.charsets.NorwegianDanish = null;
    t.charsets.Spanish = null;
    t.charsets.Swedish = null;
    t.charsets.Swiss = null;
    t.charsets.ISOLatin = null;
    function k(B, A, z, y) {
        B.addEventListener(A, z, y || false)
    }

    function w(B, A, z, y) {
        B.removeEventListener(A, z, y || false)
    }

    function d(y) {
        if (y.preventDefault) {
            y.preventDefault()
        }
        y.returnValue = false;
        if (y.stopPropagation) {
            y.stopPropagation()
        }
        y.cancelBubble = true;
        return false
    }

    function l(A, y) {
        function z() {
            this.constructor = A
        }

        z.prototype = y.prototype;
        A.prototype = new z
    }

    function v(A) {
        var z = A.getElementsByTagName("body")[0];
        var C = A.createElement("span");
        C.innerHTML = "hello world";
        z.appendChild(C);
        var B = C.scrollWidth;
        C.style.fontWeight = "bold";
        var y = C.scrollWidth;
        z.removeChild(C);
        return B !== y
    }

    var u = this.String;
    var b = this.setTimeout;
    var a = this.setInterval;

    function c(A, z) {
        var y = A.length;
        while (y--) {
            if (A[y] === z) {
                return y
            }
        }
        return -1
    }

    function r(y) {
        if (y <= "\uff00") {
            return false
        }
        return (y >= "\uff01" && y <= "\uffbe") || (y >= "\uffc2" && y <= "\uffc7") || (y >= "\uffca" && y <= "\uffcf") || (y >= "\uffd2" && y <= "\uffd7") || (y >= "\uffda" && y <= "\uffdc") || (y >= "\uffe0" && y <= "\uffe6") || (y >= "\uffe8" && y <= "\uffee")
    }

    function m(A, y, G) {
        var B = (A << 16) | (y << 8) | G;
        if (m._cache[B] != null) {
            return m._cache[B]
        }
        var C = Infinity, I = -1, D = 0, E, z, J, F, H;
        for (; D < t.vcolors.length; D++) {
            E = t.vcolors[D];
            z = E[0];
            J = E[1];
            F = E[2];
            H = m.distance(A, y, G, z, J, F);
            if (H === 0) {
                I = D;
                break
            }
            if (H < C) {
                C = H;
                I = D
            }
        }
        return m._cache[B] = I
    }

    m._cache = {};
    m.distance = function (C, B, D, z, y, A) {
        return Math.pow(30 * (C - z), 2) + Math.pow(59 * (B - y), 2) + Math.pow(11 * (D - A), 2)
    };
    function e(B, z, y) {
        if (B.forEach) {
            return B.forEach(z, y)
        }
        for (var A = 0; A < B.length; A++) {
            z.call(y, B[A], A, B)
        }
    }

    function n(A) {
        if (Object.keys) {
            return Object.keys(A)
        }
        var y, z = [];
        for (y in A) {
            if (Object.prototype.hasOwnProperty.call(A, y)) {
                z.push(y)
            }
        }
        return z
    }

    t.EventEmitter = h;
    t.inherits = l;
    t.on = k;
    t.off = w;
    t.cancel = d;
    if (typeof module !== "undefined") {
        module.exports = t
    } else {
        this.Terminal = t
    }
}).call(function () {
    return this || (typeof window !== "undefined" ? window : global)
}());