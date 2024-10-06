import React from 'react';

const StatusIndicator = ({ status }) => {
    // Fallback to an empty object if status is undefined
    const currentStatus = status || {};
    
    // Define the status steps with both case-sensitive keys
    const steps = [
        { label: 'Pending', keys: ['Pending', 'pending'] },
        { label: 'Processing', keys: ['Processing', 'processing'] },
        { label: 'Dispatched', keys: ['Dispatched', 'dispatched'] },
        { label: 'Partially Delivered', keys: ['Partially_Delivered', 'partially_Delivered'] },
        { label: 'Delivered', keys: ['Delivered', 'delivered'] },
        { label: 'Canceled', keys: ['Canceled', 'canceled'] },
    ];

    return (
        <div style={{ display: 'flex', alignItems: 'center', justifyContent: 'space-between', margin: '10px 0' }}>
            {steps.map((step, index) => (
                <div key={index} style={{ textAlign: 'center', flex: 1 }}>
                    <div style={{
                        width: '30px',
                        height: '30px',
                        borderRadius: '50%',
                        backgroundColor: step.keys.some(key => currentStatus[key]) ? 'green' : 'lightgray',
                        display: 'flex',
                        alignItems: 'center',
                        justifyContent: 'center',
                        color: 'white',
                        margin: '0 auto',
                    }}>
                        {step.keys.some(key => currentStatus[key]) ? '✓' : ''}
                    </div>
                    <div style={{ marginTop: '5px', fontSize: '12px' }}>{step.label}</div>
                </div>
            ))}
        </div>
    );
};

export default StatusIndicator;
