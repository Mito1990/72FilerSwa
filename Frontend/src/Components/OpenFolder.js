// import { useState,useEffect } from 'react';
// import Cookies from 'js-cookie';
// import {BrowserRouter,Routes,Route,Navigate,useLocation} from "react-router-dom";

// export const OpenFolder = () =>{
//     const { state } = useLocation();
//     const[data,setData] = useState(state); 
//     console.log(data);
//     return (<div className=' h-screen bg-slate-950'>hello</div>);

// }

import React from 'react';

const OpenFolder = ({ data, handleSubmit, AddFolder, OpenFolder }) => {
  return (
      <div className='flex justify-start mt-10 w-2/4 flex-wrap'>
        {data.map((item, index) => (
          <button key={index} className='flex flex-col justify-items-center m-2 w-full sm:w-1/2 md:w-1/3 lg:w-3/4 xl:w-1/5 flex-basis-full' onClick={() => OpenFolder(item)}>
            <svg className='h-9 w-9 bg-slate-500' xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512"><path d="M64 480H448c35.3 0 64-28.7 64-64V160c0-35.3-28.7-64-64-64H288c-10.1 0-19.6-4.7-25.6-12.8L243.2 57.6C231.1 41.5 212.1 32 192 32H64C28.7 32 0 60.7 0 96V416c0 35.3 28.7 64 64 64z"/></svg>        
            <div className='h-5 w-9' key={index}>
              {/* {item.name} */}
              {item.name}
            </div>
          </button>
        ))}
      </div>
  );
}

export default OpenFolder;
