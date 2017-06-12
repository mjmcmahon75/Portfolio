/**
 * 
 * @author:  Edward Angel
 * Modified by Marietta E. Cameron
 * Last Modified: 1-26-2016
 * 
 * Modified by Max McMahon, Steven Borowski
 * Last Modified 4/26/16
 * 
 * Graphically Represents a Binary Search Tree
 */


var gl;
var points;
var keys = new Array();
var ctx;

function canvasMain() {
    var canvas = document.getElementById("gl-canvas"); //must be in html
    var textCanvas = document.getElementById("textCanvas"); //must be in html

    ctx = textCanvas.getContext("2d");
    ctx.font = "20px Arial";

    gl = WebGLUtils.setupWebGL(canvas);
    if (!gl) {
        alert("WebGL isn't available");
    }
    var objColor = [0, 0, .5, 1.0];

    //  Configure WebGL

    gl.viewport(0, 0, canvas.width, canvas.height);
    gl.clearColor(1.0, 1.0, 1.0, 1.0);

    //  Load shaders and initialize attribute buffers

    var program = initShaders(gl, "vertex-shader", "fragment-shader");
    gl.useProgram(program);
    gl.clear(gl.COLOR_BUFFER_BIT);


    //test area
    keys = generateKeys();
    BST(keys);

    function BST(keys) {

        //create nodes from keylist
        var nodes = new Array();
        for (i = 0; i < keys.length; i++)
        {
            addNode(keys[i]);
        }
        var cdAr = coordStor();
        drawBST();
//test statement
        for (z = 0; z < nodes.length; z++)
        {
            console.log(nodes[z].key, nodes[z].left, nodes[z].right);
        }

        //node function type contains key, parent, left child and right child
        function node(key, parent) {
            this.key = key;
            this.parent = parent;
            this.left = null;
            this.right = null;
        }
        ;

        //main function for adding new nodes, new nodes stored in nodes array
        function addNode(key)
        {
            //checks to see if key is already present to avoid duplicates

            //empty tree case, makes new node root
            if (nodes[0] == null)
            {
                nodes[0] = new node(key, null)
            }

            //cycles through tree until new node finds suitable parent
            else
            {
                var pF = false;
                var x = 0;
                while (pF == false)
                {
                    if (key < nodes[x].key)
                    {
                        //room for left child
                        if (nodes[x].left == null)
                        {
                            nodes[x].left = key;
                            pF = true;
                        }
                        //no room for left child
                        else
                        {
                            x = getNode(nodes[x].left);
                        }
                    }
                    else
                    {
                        //room for right child
                        if (nodes[x].right == null)
                        {
                            nodes[x].right = key;
                            pF = true;
                        }
                        //no room for right child
                        else
                        {
                            x = getNode(nodes[x].right);
                        }
                    }
                }
                nodes[i] = new node(key, x);
            }

        }
        //finds node from key values used primarily to get next node down
        //x is not bound, USE ONLY WHEN KEY EXISITS
        function getNode(sKey)
        {
            var x = 0;
            var search = null;
            var found = false;
            while (found == false)
            {
                if (nodes[x].key == sKey)
                {
                    search = nodes[x];
                    found = true;
                }
                else
                    x++;
            }
            return x;
        }
        function coordStor()
        {
            var y = new Array(4);
            for (j = 0; j < y.length; j++)
            {
                y[j] = new Array();
                for (k = 0; k < 15; k++)
                {
                    y[j][k] = null;
                }
            }
            y[0][7] = nodes[0];
            for (j = 0; j < y.length; j++)
            {
                for (k = 0; k < y[j].length; k++)
                {
                    if (y[j][k] !== null)
                    {
                        if (j === y.length - 1)
                        {
                            y[j][k].left = null;
                            y[j][k].right = null;
                        }
                        else
                        {
                            if ((y[j][k].left) != null)
                            {
                                y[j + 1][k - Math.pow(2, (2 - j))] = nodes[getNode(y[j][k].left)];
                            }
                            if ((y[j][k].right) != null)
                            {
                                y[j + 1][k + Math.pow(2, (2 - j))] = nodes[getNode(y[j][k].right)];
                            }

                        }
                    }
                }
            }
            return y;
        }

        //draw function, uses coordinates in nodeLocs to determine placement position, also calculates positions for branch lines
        function drawBST()
        {

            var radius = ((2 - .0025) / 15) / 2;
            var nY = 1 - (.0025 + (2 * radius));
            for (j = 0; j < cdAr.length; j++)
            {
                var nX = (-1 + (.0025 + radius));
                for (k = 0; k < cdAr[j].length; k++)
                {
                    if (cdAr[j][k] != null)
                    {
                        var currKey = cdAr[j][k].key;

                        if (cdAr[j][k].left != null)
                        {
                            objColor = [0, 0, 0, 1];
                            var lineCX = nX - ((2 * radius) * (Math.pow(2, (2 - j))));
                            var lineCY = (nY - (7 * radius)) + radius;
                            var linePY = nY - radius;
                            var branch = genBranch(nX, linePY, lineCX, lineCY);

                            drawObject(gl, program, branch, objColor, gl.LINE_LOOP);
                        }
                        if (cdAr[j][k].right != null)
                        {
                            var lineCX = nX + ((2 * radius) * (Math.pow(2, (2 - j))));
                            var lineCY = (nY - (7 * radius)) + radius;
                            var linePY = nY - radius;
                           

                            var branch = genBranch(nX, linePY, lineCX, lineCY);

                            drawObject(gl, program, branch, objColor, gl.LINE_LOOP);
                        }
                        drawNode(nX, nY, currKey);

                        //drawline
                    }
                    nX += 2 * radius;
                }
                nY -= (7 * radius);
            }

            function genBranch(xT, yT, xB, yB)
            {
                var branchVert = [];

                branchVert.push(vec2(xT, yT));
                branchVert.push(vec2(xB, yB));

                return branchVert;
            }
            //draw circle centered at (nodeLocs[j].x, nodeLocs[j].y,

            //draw text in circle with same center as above, use (nodeLocs[j].node.key) for number
        }

        //line mating

    }


    function drawNode(xCord, yCord, number) {
        var objColor = [0, 0, 0, 1];

        drawObject(gl, program, generateCircle(.06, 50, xCord, yCord), objColor, gl.LINE_LOOP);

        ctx.fillText("" + number, Math.floor(getX(xCord)), Math.floor(getY(yCord)));
    }

}

;//CanvasMain

function generateKeys() {
    var keys = [];
    var size = 10;

    for (var i = 0; i < size; i++) {
        keys[i] = i + 1;
    }

    shuffle(keys);

    return keys;
}

function shuffle(array) {
    var currentIndex = array.length, temporaryValue, randomIndex;

    // While there remain elements to shuffle...
    while (0 !== currentIndex) {

        // Pick a remaining element...
        randomIndex = Math.floor(Math.random() * currentIndex);
        currentIndex -= 1;

        // And swap it with the current element.
        temporaryValue = array[currentIndex];
        array[currentIndex] = array[randomIndex];
        array[randomIndex] = temporaryValue;
    }

    return array;
}
//Modified to be pushed from the origin
function generateCircle(radius, pointCount, xOrg, yOrg) {
    var circleVertices = [];
    var inc = 2 * Math.PI / pointCount;

    for (var theta = 0; theta < 2 * Math.PI; theta += inc) {
        circleVertices.push(vec2(radius * Math.cos(theta) + xOrg, radius * Math.sin(theta) + yOrg));
    }//generate circle

    return circleVertices;
}
;//generateCircle

//Primary BST function takes array type keylist, and creates a binary search tree from the list
// Functions:
// 
// addNode: new nodes
// getNode: int -> node
// coordStor() stores into 2d array
// drawNode: draw BST
// 
// 
// "Classes":
// 
// node: primary node, contains key, parent, etc
// nodeLoc: stores node position data
//


//replaces contains curates array for duplicate inputs

function drawObject(gl, program, vertices, color, glType) {
    var colorLocation = gl.getUniformLocation(program, "uColor");


    var bufferId = gl.createBuffer();
    gl.bindBuffer(gl.ARRAY_BUFFER, bufferId);
    gl.bufferData(gl.ARRAY_BUFFER, flatten(vertices), gl.STATIC_DRAW);


    // Associate out shader variables with our data buffer

    var vPosition = gl.getAttribLocation(program, "vPosition");
    gl.vertexAttribPointer(vPosition, 2, gl.FLOAT, false, 0, 0);
    gl.enableVertexAttribArray(vPosition);





    // set uniform color.
    gl.uniform4f(colorLocation, color[0], color[1], color[2], color[3]);

    gl.drawArrays(glType, 0, vertices.length);


}


function getX(x) {
    x -= 0.005
    var w = 256 * (x + 1);
    return w;
}

function getY(y) {
    y -= 0.05;
    var h = 256 * (1 - y);
    return h;
}





