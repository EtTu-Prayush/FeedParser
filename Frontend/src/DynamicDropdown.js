
import React, { useState, useEffect } from 'react';
import { TreeSelect } from 'antd';
import axios from 'axios';

const { TreeNode } = TreeSelect;

const DynamicDropdown = ({ jsonContent, onChange }) => {
    const [treeData, setTreeData] = useState([]);
    const [value, setValue] = useState(undefined);

    useEffect(() => {
        if (jsonContent) {
            fetchParsedJson();
        }
    }, [jsonContent]);

    const fetchParsedJson = async () => {
        try {
            const response = await axios.post('http://localhost:4000/api/parse-json', jsonContent, {
                headers: {
                    'Content-Type': 'application/json'
                }
            });
            // console.log('Parsed JSON:', response.data);
            
            const parsedData = transformToTreeData(response.data);
            setTreeData(parsedData);
        } catch (error) {
            console.error('Error fetching parsed JSON:', error);
        }
    };

    const transformToTreeData = (data) => {
        const treeData = [];
        const addNode = (keys, value) => {
            let currentLevel = treeData;
            keys.forEach((key, index) => {
                let existingNode = currentLevel.find(node => node.title === key);
                if (!existingNode) {
                    existingNode = { title: key, value: keys.slice(0, index + 1).join('.'), children: [] };
                    currentLevel.push(existingNode);
                }
                if (index === keys.length - 1) {
                    existingNode.value = value;
                } else {
                    currentLevel = existingNode.children;
                }
            });
        };

        Object.entries(data).forEach(([key, value]) => {
            const keys = key.split('.');
            addNode(keys, key);
        });

        return treeData;
    };

    const onChangeTree = (value) => {
        setValue(value);
        onChange(value);
    };
    return (
        <TreeSelect
            style={{ width: '100%' }}
            value={value}
            dropdownStyle={{ maxHeight: 400, overflow: 'auto' }}
            placeholder="Select field"
            treeDefaultExpandAll
            onChange={onChangeTree}
        >
            {renderTreeNodes(treeData)}
        </TreeSelect>
    );
};

const renderTreeNodes = data =>
    data.map(item => {
        if (item.children) {
            return (
                <TreeNode title={item.title} value={item.value} key={item.value}>
                    {renderTreeNodes(item.children)}
                </TreeNode>
            );
        }
        return <TreeNode key={item.value} {...item} />;
    });

export default DynamicDropdown;
