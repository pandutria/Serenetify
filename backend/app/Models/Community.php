<?php

namespace App\Models;

use Illuminate\Database\Eloquent\Factories\HasFactory;
use Illuminate\Database\Eloquent\Model;

class Community extends Model
{
    use HasFactory;

    protected $fillable = [
        'name',
        'description',
        'image_url',
        'logo_url',
    ];

    public function messages()
    {
        return $this->hasMany(CommunityMessage::class, 'community_id');
    }
}
