let currentFile = null;

// 提取设置图片 src 属性的逻辑
function setImageSrc(img, filename) {
    img.src = `/image?filename=${encodeURIComponent(filename)}&t=${Date.now()}`;
}

//异步函数，接收一个文件名作为参数
async function loadFile(filename) {
    currentFile = filename;
    try {
        if (filename.match(/\.(jpg|jpeg|png|gif)$/i)) {
            document.getElementById('editor').style.display = 'none';
            const img = document.getElementById('imagePreview');
            img.style.display = 'block';
            setImageSrc(img, filename);
        } else {
            document.getElementById('imagePreview').style.display = 'none';
            const editor = document.getElementById('editor');
            editor.style.display = 'block';
            const response = await fetch(`/content?filename=${encodeURIComponent(filename)}`);
            if (!response.ok) {
                //提示框显示错误信息
                throw new Error('此文件不是图片或文档');
            }
            editor.value = await response.text();
        }
    } catch (error) {
        alert(`加载文件时出错：${error.message}`);
    }
}

function loadImage(filename) {
    const img = document.getElementById('imagePreview');
    img.style.display = 'block';
    setImageSrc(img, filename);
}

async function saveFile() {

    //if (!currentFile) return;
    // const content = document.getElementById('editor').value;
    // try {
    //     const response = await fetch('/save', {
    //         method: 'POST',
    //         headers: { 'Content-Type': 'text/plain' },
    //         body: new URLSearchParams({
    //             filename: currentFile,
    //             content: content
    //         })
    //     });
    //     if (!response.ok) {
    //         throw new Error('文件保存失败');
    //     }
    //     alert('保存成功！');
    // } catch (error) {
    //     alert(`保存文件时出错：${error.message}`);
    // }

    const content = document.getElementById('editor').value;
    if (!currentFile) {
        alert('请先选择一个文件！');
        return;
    }

    await fetch('/save', {
        method: 'POST',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify({ filename: currentFile, content })
    });
    alert('保存成功！');
}

function isImageFile(filename) {
    return filename.match(/\.(jpg|jpeg|png|gif|webp|bmp)$/i);
}