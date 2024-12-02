<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Welcome!</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/static/style.css">
    <script>
        function handleSubmit(event) {
            event.preventDefault();

            const userInput = document.getElementById('userPrompt').value;

            if (!userInput.trim()) {
                alert('Please enter a text!');
                return;
            }

            const container = document.querySelector('.container');

            const loadingText = document.createElement('p');
            loadingText.textContent = 'Generating story, please wait...';
            loadingText.style.marginTop = '20px';
            loadingText.style.fontWeight = 'bold';
            loadingText.style.fontSize = '16px';

            container.appendChild(loadingText);

            const img = document.createElement('img');
            img.src = '${pageContext.request.contextPath}/images/bonfire-animation.gif'
            img.alt = '${pageContext.request.contextPath}/images/red-warning-circle.png';
            img.style.marginTop = '10px';
            container.appendChild(img);

            document.getElementById('userForm').submit();
        }

        function toggleFileUpload() {
            const fileUploadBlock = document.getElementById('fileUploadBlock');
            fileUploadBlock.style.display = fileUploadBlock.style.display === 'none' ? 'block' : 'none';
        }
    </script>
</head>
<body>
    <div class="container">
        <h1>Welcome to Quest Game!</h1>
        <p>Write something and AI will create unique story</p>
        <form id="userForm" action="${pageContext.request.contextPath}/loading" method="post" target="_self" onsubmit="handleSubmit(event)">
            <label>
                <input id="userPrompt" name="userPrompt" type="text" placeholder="type here, e.g: Рыцарь и Дракон" required>
                <button type="submit">Generate story</button>
            </label>
        </form>

        <button type="button" onclick="toggleFileUpload()">Load story file</button>

        <div id="fileUploadBlock">
            <form id="fileUploadForm" action="${pageContext.request.contextPath}/upload" method="post" enctype="multipart/form-data">
                <label for="fileInput">Choose a file to upload:</label>
                <input id="fileInput" name="file" type="file" accept=".ser" required>
                <button type="submit">Upload story</button>
            </form>
        </div>
    </div>
</body>
</html>
