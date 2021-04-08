var counter = 10000; // very big to not collide with loop.index when reloading previous text content

function moreFields() {
    counter++;
    var newFields = document.getElementById('readroot').cloneNode(true);
    newFields.id = '';
    newFields.style.display = 'block';
    var newField = newFields.childNodes;
    for (var i = 0; i < newField.length; i++) {
        var theName = newField[i].name
        if (theName)
            newField[i].name = theName + '_' + counter;
    }
    var insertHere = document.getElementById('writeroot');
    // create wrapper container
    var wrapper = document.createElement('aside');
    // insert wrapper before newFields in the DOM tree
    insertHere.parentNode.insertBefore(wrapper, insertHere);
    // move newFields into wrapper
    wrapper.appendChild(newFields);
}

window.onload = moreFields;