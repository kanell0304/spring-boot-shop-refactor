import React, { useRef, useEffect } from 'react';
import { Editor } from '@toast-ui/react-editor';
import '@toast-ui/editor/dist/toastui-editor.css';

const EditorComponent = ({ value, onChange }) => {
  const editorRef = useRef();

  // 부모로부터 받은 값 반영
  useEffect(() => {
    if (editorRef.current) {
      const editor = editorRef.current.getInstance();
      const currentHTML = editor.getHTML();
      if (currentHTML !== value) {
        editor.setHTML(value || ''); // HTML로 세팅
      }
    }
  }, [value]);

  const handleChange = () => {
    const content = editorRef.current.getInstance().getHTML();
    onChange(content); // HTML 형태로 업데이트
  };

  return (
    <div className="editorWrap">
      <Editor
        initialValue="" 
        previewStyle="vertical"
        height="400px"
        initialEditType="wysiwyg" // 위지윅 모드
        useCommandShortcut={true}
        onChange={handleChange}
        ref={editorRef}
        hideModeSwitch={true}
      />
    </div>
  );
};

export default EditorComponent;
