import React, { useState } from 'react';

const Folder = ({ name, folders }) => {
  const [isOpen, setIsOpen] = useState(false);

  const toggleFolder = () => {
    setIsOpen(!isOpen);
  };

  return (
    <div>
      <div onClick={toggleFolder}>
        <span>{isOpen ? '▼' : '►'}</span>
        <span>{name}</span>
      </div>
      {isOpen && (
        <ul>
          {folders.map((folder) => (
            <li key={folder.id}>
              <Folder name={folder.name} folders={folder.folders} />
            </li>
          ))}
        </ul>
      )}
    </div>
  );
};

const FolderStructure = () => {
  const initialFolders = [
    {
      id: 1,
      name: 'Folder 1',
      folders: [
        {
          id: 11,
          name: 'Subfolder 1.1',
          folders: [
            { id: 111, name: 'Subfolder 1.1.1', folders: [] },
            { id: 112, name: 'Subfolder 1.1.2', folders: [] },
          ],
        },
        { id: 12, name: 'Subfolder 1.2', folders: [] },
      ],
    },
    { id: 2, name: 'Folder 2', folders: [] },
    { id: 3, name: 'Folder 3', folders: [] },
  ];

  return (
    <div>
      <h2>Folder Structure</h2>
      <ul>
        {initialFolders.map((folder) => (
          <li key={folder.id}>
            <Folder name={folder.name} folders={folder.folders} />
          </li>
        ))}
      </ul>
    </div>
  );
};

export default FolderStructure;
