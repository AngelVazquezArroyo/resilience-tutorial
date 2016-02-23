/* Generated by Opal 0.8.0.rc2 */
Opal.modules["stringio"] = function(Opal) {
  Opal.dynamic_require_severity = "warning";
  function $rb_ge(lhs, rhs) {
    return (typeof(lhs) === 'number' && typeof(rhs) === 'number') ? lhs >= rhs : lhs['$>='](rhs);
  }
  function $rb_gt(lhs, rhs) {
    return (typeof(lhs) === 'number' && typeof(rhs) === 'number') ? lhs > rhs : lhs['$>'](rhs);
  }
  function $rb_plus(lhs, rhs) {
    return (typeof(lhs) === 'number' && typeof(rhs) === 'number') ? lhs + rhs : lhs['$+'](rhs);
  }
  function $rb_minus(lhs, rhs) {
    return (typeof(lhs) === 'number' && typeof(rhs) === 'number') ? lhs - rhs : lhs['$-'](rhs);
  }
  var self = Opal.top, $scope = Opal, nil = Opal.nil, $breaker = Opal.breaker, $slice = Opal.slice, $klass = Opal.klass, $range = Opal.range;

  Opal.add_stubs(['$include', '$new', '$call', '$close', '$attr_accessor', '$length', '$include?', '$!', '$check_readable', '$==', '$===', '$raise', '$seek', '$enum_for', '$eof?', '$ord', '$[]', '$check_writable', '$String', '$write', '$closed_write?', '$closed_read?']);
  return (function($base, $super) {
    function $StringIO(){};
    var self = $StringIO = $klass($base, $super, 'StringIO', $StringIO);

    var def = self.$$proto, $scope = self.$$scope, TMP_1, TMP_2, TMP_3;

    def.position = def.string = def.closed = nil;
    self.$include((($scope.get('IO')).$$scope.get('Readable')));

    self.$include((($scope.get('IO')).$$scope.get('Writable')));

    Opal.defs(self, '$open', TMP_1 = function(string, mode) {
      var self = this, $iter = TMP_1.$$p, block = $iter || nil, io = nil, res = nil;

      if (string == null) {
        string = ""
      }
      if (mode == null) {
        mode = nil
      }
      TMP_1.$$p = null;
      io = self.$new(string, mode);
      res = block.$call(io);
      io.$close();
      return res;
    });

    self.$attr_accessor("string");

    def.$initialize = function(string, mode) {
      var $a, $b, self = this;

      if (string == null) {
        string = ""
      }
      if (mode == null) {
        mode = "rw"
      }
      self.string = string;
      self.position = string.$length();
      if ((($a = ($b = mode['$include?']("r"), $b !== false && $b !== nil ?mode['$include?']("w")['$!']() : $b)) !== nil && (!$a.$$is_boolean || $a == true))) {
        return self.closed = "write"
      } else if ((($a = ($b = mode['$include?']("w"), $b !== false && $b !== nil ?mode['$include?']("r")['$!']() : $b)) !== nil && (!$a.$$is_boolean || $a == true))) {
        return self.closed = "read"
        } else {
        return nil
      };
    };

    def['$eof?'] = function() {
      var self = this;

      self.$check_readable();
      return self.position['$=='](self.string.$length());
    };

    Opal.defn(self, '$eof', def['$eof?']);

    def.$seek = function(pos, whence) {
      var self = this, $case = nil;

      if (whence == null) {
        whence = (($scope.get('IO')).$$scope.get('SEEK_SET'))
      }
      $case = whence;if ((($scope.get('IO')).$$scope.get('SEEK_SET'))['$===']($case)) {if ($rb_ge(pos, 0)) {
        } else {
        self.$raise((($scope.get('Errno')).$$scope.get('EINVAL')))
      };
      self.position = pos;}else if ((($scope.get('IO')).$$scope.get('SEEK_CUR'))['$===']($case)) {if ($rb_gt($rb_plus(self.position, pos), self.string.$length())) {
        self.position = self.string.$length()
        } else {
        self.position = $rb_plus(self.position, pos)
      }}else if ((($scope.get('IO')).$$scope.get('SEEK_END'))['$===']($case)) {if ($rb_gt(pos, self.string.$length())) {
        self.position = 0
        } else {
        self.position = $rb_minus(self.position, pos)
      }};
      return 0;
    };

    def.$tell = function() {
      var self = this;

      return self.position;
    };

    Opal.defn(self, '$pos', def.$tell);

    Opal.defn(self, '$pos=', def.$seek);

    def.$rewind = function() {
      var self = this;

      return self.$seek(0);
    };

    def.$each_byte = TMP_2 = function() {
      var $a, $b, self = this, $iter = TMP_2.$$p, block = $iter || nil, i = nil;

      TMP_2.$$p = null;
      if (block !== false && block !== nil) {
        } else {
        return self.$enum_for("each_byte")
      };
      self.$check_readable();
      i = self.position;
      while (!((($b = self['$eof?']()) !== nil && (!$b.$$is_boolean || $b == true)))) {
      block.$call(self.string['$[]'](i).$ord());
      i = $rb_plus(i, 1);};
      return self;
    };

    def.$each_char = TMP_3 = function() {
      var $a, $b, self = this, $iter = TMP_3.$$p, block = $iter || nil, i = nil;

      TMP_3.$$p = null;
      if (block !== false && block !== nil) {
        } else {
        return self.$enum_for("each_char")
      };
      self.$check_readable();
      i = self.position;
      while (!((($b = self['$eof?']()) !== nil && (!$b.$$is_boolean || $b == true)))) {
      block.$call(self.string['$[]'](i));
      i = $rb_plus(i, 1);};
      return self;
    };

    def.$write = function(string) {
      var self = this, before = nil, after = nil;

      self.$check_writable();
      string = self.$String(string);
      if (self.string.$length()['$=='](self.position)) {
        self.string = $rb_plus(self.string, string);
        return self.position = $rb_plus(self.position, string.$length());
        } else {
        before = self.string['$[]']($range(0, $rb_minus(self.position, 1), false));
        after = self.string['$[]']($range($rb_plus(self.position, string.$length()), -1, false));
        self.string = $rb_plus($rb_plus(before, string), after);
        return self.position = $rb_plus(self.position, string.$length());
      };
    };

    def.$read = function(length, outbuf) {
      var $a, self = this, string = nil, str = nil;

      if (length == null) {
        length = nil
      }
      if (outbuf == null) {
        outbuf = nil
      }
      self.$check_readable();
      if ((($a = self['$eof?']()) !== nil && (!$a.$$is_boolean || $a == true))) {
        return nil};
      string = (function() {if (length !== false && length !== nil) {
        str = self.string['$[]'](self.position, length);
        self.position = $rb_plus(self.position, length);
        return str;
        } else {
        str = self.string['$[]']($range(self.position, -1, false));
        self.position = self.string.$length();
        return str;
      }; return nil; })();
      if (outbuf !== false && outbuf !== nil) {
        return outbuf.$write(string)
        } else {
        return string
      };
    };

    def.$close = function() {
      var self = this;

      return self.closed = "both";
    };

    def.$close_read = function() {
      var self = this;

      if (self.closed['$==']("write")) {
        return self.closed = "both"
        } else {
        return self.closed = "read"
      };
    };

    def.$close_write = function() {
      var self = this;

      if (self.closed['$==']("read")) {
        return self.closed = "both"
        } else {
        return self.closed = "write"
      };
    };

    def['$closed?'] = function() {
      var self = this;

      return self.closed['$==']("both");
    };

    def['$closed_read?'] = function() {
      var $a, self = this;

      return ((($a = self.closed['$==']("read")) !== false && $a !== nil) ? $a : self.closed['$==']("both"));
    };

    def['$closed_write?'] = function() {
      var $a, self = this;

      return ((($a = self.closed['$==']("write")) !== false && $a !== nil) ? $a : self.closed['$==']("both"));
    };

    def.$check_writable = function() {
      var $a, self = this;

      if ((($a = self['$closed_write?']()) !== nil && (!$a.$$is_boolean || $a == true))) {
        return self.$raise($scope.get('IOError'), "not opened for writing")
        } else {
        return nil
      };
    };

    return (def.$check_readable = function() {
      var $a, self = this;

      if ((($a = self['$closed_read?']()) !== nil && (!$a.$$is_boolean || $a == true))) {
        return self.$raise($scope.get('IOError'), "not opened for reading")
        } else {
        return nil
      };
    }, nil) && 'check_readable';
  })(self, $scope.get('IO'))
};
