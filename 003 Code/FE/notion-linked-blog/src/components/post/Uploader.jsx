import React, {useEffect, useState} from 'react';
import {Button} from "antd";

const Uploader = (props) => {

  const [image, setImage] = useState({
    image_file: props.thumbImage.image_file,
    preview_URL: props.thumbImage.preview_URL,
  });

  let inputRef;

  const saveImage = (e) => {
    // e.preventDefault();
    if(e.target.files[0]){
      // 새로운 이미지를 올리면 createObjectURL()을 통해 생성한 기존 URL을 폐기
      URL.revokeObjectURL(image.preview_URL);
      const preview_URL = URL.createObjectURL(e.target.files[0]);
      setImage(() => (
        {
          image_file: e.target.files[0],
          preview_URL: preview_URL
        }
      ))
    }
  }

  const deleteImage = () => {
    // createObjectURL()을 통해 생성한 기존 URL을 폐기
    URL.revokeObjectURL(image.preview_URL);
    setImage({
      image_file: "",
      preview_URL: "",
    });
  }

  useEffect(() => {
    props.changeThumbImage(image);
  }, [image]);

  useEffect(()=> {
    // 컴포넌트가 언마운트되면 createObjectURL()을 통해 생성한 기존 URL을 폐기
    return () => {
      URL.revokeObjectURL(image.preview_URL)
    }
  }, [])

  return (
    <div className="uploader-wrapper" style={{display:"flex", flexDirection:"column"}}>
      <input type="file" accept="image/*"
             onChange={saveImage}
        // 클릭할 때 마다 file input의 value를 초기화 하지 않으면 버그가 발생할 수 있다
        // 사진 등록을 두개 띄우고 첫번째에 사진을 올리고 지우고 두번째에 같은 사진을 올리면 그 값이 남아있음!
             onClick={(e) => e.target.value = null}
             ref={refParam => inputRef = refParam}
             style={{display: "none"}}
      />
      <div className="img-wrapper">
        <img src={image.preview_URL}  style={{width:"200px", height:"200px"}}/>
      </div>

      <div className="upload-button" style={{display:"flex", flexDirection:"column"}}>
        <Button type="primary" variant="contained" onClick={() => inputRef.click()} style={{width:"100%"}}>
          이미지 업로드
        </Button>
        <Button color="error" variant="contained" onClick={deleteImage} style={{width:"100%"}}>
          이미지 제거
        </Button>
      </div>
    </div>
  );
};

export default Uploader;